package com.yunxiao.service.demooauth.cors;

import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @author LuoYunXiao
 * @since 2023/12/28 15:03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(CorsProperties.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public @interface EnableCors {
}
