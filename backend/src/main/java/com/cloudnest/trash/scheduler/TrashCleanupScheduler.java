package com.cloudnest.trash.scheduler;

import com.cloudnest.trash.service.TrashCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrashCleanupScheduler {

    private final TrashCleanupService trashCleanupService;

    @Scheduled(cron = "0 * * * * *")
    public void cleanupTrash() {

        try {

            trashCleanupService.cleanup();

            log.info("Trash cleanup completed successfully.");

        } catch (Exception e) {

            log.error("Trash cleanup failed.", e);

        }
    }
}