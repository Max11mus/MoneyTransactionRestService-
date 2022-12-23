package main.java.com.foxminded.money.logging;

import main.java.com.foxminded.money.exeptions.ServiceUnavailableException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TransactionsAspectLogger {
    private final static Logger LOGGER = LoggerFactory.getLogger(TransactionsAspectLogger.class);

    @Pointcut("within(com.foxminded.money.service.*)")
    public void anyTransaction(){
    }

    @Around("anyTransaction()")
    public Object logTransaction(ProceedingJoinPoint point) throws Throwable {
        LOGGER.info("Start Transaction");

        LOGGER.info("Method: ");

        String signature = point.getSignature().toLongString();
        LOGGER.info(signature);

        LOGGER.info("Arguments: ");
        Object[] args = point.getArgs();
        for (Object arg : args) {
            LOGGER.info(arg.toString());
        }

        Object result = null;
        try {
            result = point.proceed();
            LOGGER.info("Result: ");

            if (result != null) {
                LOGGER.info(result.toString());
            } else {
                LOGGER.info("void");
            }

            LOGGER.info("End Transaction");
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
            LOGGER.error("Unroll Transaction");
            throw new ServiceUnavailableException(e.getMessage(), e);
        }
        return result;
    }

}
