package org.example.pfebackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.Map;

@RestController
@CrossOrigin("*")
public class StripeController {

    @PostMapping("/create-checkout-session/{id}")
    public ResponseEntity<String> createCheckoutSession(@PathVariable Integer id) {

        try {

            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("http://localhost:4200/payment-success/" + id)
                            .setCancelUrl("http://localhost:4200/payment-cancel")
                            .putAllMetadata(Map.of("appointmentId", id.toString()))
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPriceData(
                                                    SessionCreateParams.LineItem.PriceData.builder()
                                                            .setCurrency("eur")
                                                            .setUnitAmount(5000L) // 50€
                                                            .setProductData(
                                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                            .setName("Paiement rendez-vous")
                                                                            .build()
                                                            )
                                                            .build()
                                            )
                                            .build()
                            )
                            .build();

            Session session = Session.create(params);

            return ResponseEntity.ok(session.getUrl());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur Stripe");
        }
    }
}
