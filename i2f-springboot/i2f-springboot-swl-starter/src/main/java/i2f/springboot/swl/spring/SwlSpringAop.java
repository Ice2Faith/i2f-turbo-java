package i2f.springboot.swl.spring;


import i2f.springboot.swl.filter.SwlWebConsts;
import i2f.springboot.swl.filter.SwlWebCtrl;
import i2f.swl.consts.SwlCode;
import i2f.swl.exception.SwlException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Ice2Faith
 * @date 2022/6/29 13:59
 * @desc
 */
@ConditionalOnClass(Aspect.class)
//@ConditionalOnBean(SwlTransfer.class)
@ConditionalOnExpression("${i2f.swl.aop.enable:true}")
@Slf4j
@Aspect
public class SwlSpringAop implements InitializingBean {

    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired
    private SwlWebConfigProperties webProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("SecureTransferAop config done.");
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void controllerAspect() {
    }

    @Around("controllerAspect()")
    public Object controllerLog(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature ms = (MethodSignature) pjp.getSignature();
        Method method = ms.getMethod();
        Class clazz = method.getDeclaringClass();

        Parameter[] params = method.getParameters();
        Object[] args = pjp.getArgs();

        log.debug("enter mapping aop:" + clazz.getName() + "." + method.getName());

        // 如果过滤器中有异常，在AOP中进行抛出，以在ExceptionHandler中进行捕获处理
        Throwable ex = (Throwable) request.getAttribute(SwlWebConsts.SWL_REQUEST_DECRYPT_EXCEPTION_ATTR_KEY);
        if (ex != null) {
            throw ex;
        }

        // 特殊标记返回值为String的方法
        Class<?> returnType = method.getReturnType();
        if (String.class.isAssignableFrom(returnType)) {
            request.setAttribute(SwlWebConsts.SWL_STRING_RESPONSE,true);
            log.debug("mapping return string type.");
        }

        SwlWebCtrl ctrl = SwlSpringWebFilter.parseCtrl(request, method, webProperties);
        if (ctrl.isIn()) {
            log.debug("request secure require.");
            Object decrypted = request.getAttribute(SwlWebConsts.SWL_REQUEST_DECRYPT_ATTR_KEY);
            if (!Boolean.TRUE.equals(decrypted)) {
                log.debug("not decrypt request error.");
                throw new SwlException(SwlCode.SYMMETRIC_EXCEPTION.code(), "不安全的请求");
            }
        }
        if (ctrl.isOut()) {
            request.setAttribute(SwlWebConsts.SWL_REQUIRE_ENCRYPT_RESPONSE, true);
        }

        log.debug("aop process...");
        Object ret = pjp.proceed();

        log.debug("aop process done.");
        // 特殊标记返回值为String的方法
        if (ret != null && (ret instanceof String)) {
            log.debug("actual return string type.");
            request.setAttribute(SwlWebConsts.SWL_STRING_RESPONSE, true);
        }
        log.debug("leave mapping aop.");
        return ret;
    }

}
