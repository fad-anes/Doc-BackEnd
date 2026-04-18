package org.example.pfebackend.Service;

import org.example.pfebackend.Dto.NotificationDto;
import org.example.pfebackend.Entity.Notification;
import org.example.pfebackend.Repository.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    NotificationRepo notificationRepo;

    public List<Notification> getAllNotificationsByUser(Integer id,String type) {
        return notificationRepo.findByIdReceverAndType(id,type);
    }

    public boolean MarkSeen(Integer id){
        Notification notification = notificationRepo.findById(id).get();
        if(notification!=null){
            notification.setSeen(true);
            notificationRepo.save(notification);
            return true;
        }
        return false;
    }

    public boolean delete(Integer id){
        Notification notification = notificationRepo.findById(id).get();
        if(notification!=null){
            notificationRepo.delete(notification);
            return true;
        }
        return false;
    }

    public Notification AddNotification(NotificationDto n) {
        Notification notification = new Notification();
        notification.setType(n.getType());
        notification.setMessage(n.getMessage());
        notification.setIdRecever(n.getIdRecever());
        notificationRepo.save(notification);
        return notification;
    }

}
