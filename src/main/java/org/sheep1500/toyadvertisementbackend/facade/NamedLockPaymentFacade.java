package org.sheep1500.toyadvertisementbackend.facade;

import lombok.RequiredArgsConstructor;
import org.sheep1500.payment.application.CancelPaymentService;
import org.sheep1500.payment.application.SuccessPaymentService;
import org.sheep1500.payment.application.dto.PaymentDto;
import org.sheep1500.payment.lock.LockManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockPaymentFacade {
    private final LockManager lockManager;
    private final SuccessPaymentService successPaymentService;
    private final CancelPaymentService cancelPaymentService;

    @Transactional
    public PaymentDto.Completed successPay(PaymentDto.Pay dto) {
        String key = dto.getCardNumber();
        try {
            lockManager.tryLock("PAYMENT", key);
            return successPaymentService.successPay(dto);
        } finally {
            lockManager.releaseLock("PAYMENT", key);
        }
    }

    @Transactional
    public PaymentDto.Completed cancelPay(PaymentDto.Cancel dto) {
        String key = dto.getPaymentId();
        try {
            lockManager.tryLock("CANCEL", key);
            return cancelPaymentService.cancelPay(dto);
        } finally {
            lockManager.releaseLock("CANCEL", key);
        }
    }
}
