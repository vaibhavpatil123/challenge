package com.db.awmd.challenge.service.impl;

import com.db.awmd.challenge.api.v1.account.model.Notification;
import com.db.awmd.challenge.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailNotificationService implements NotificationService {
  @Override
  public void notifyAboutTransfer(Notification notification) {
    log.info("Sending notification to user {0} ", notification.toString());
  }
}

// ~ Formatted by Jindent --- http://www.jindent.com
