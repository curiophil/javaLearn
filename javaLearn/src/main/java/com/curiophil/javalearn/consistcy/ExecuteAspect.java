package com.curiophil.javalearn.consistcy;

import com.curiophil.javalearn.util.TransactionThreadLocal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Aspect
@Component
public class ExecuteAspect {

    @Autowired
    private ApplicationContext applicationContext;

    @Around("@annotation(execute)")
    public Object around(ProceedingJoinPoint joinPoint, Execute execute) throws Throwable {
        if (TransactionThreadLocal.getTxId() == null) {
            TransactionThreadLocal.setTxId(UUID.randomUUID().toString());
            // 向数据库添加事务，设置状态为waiting
        }

        String name = joinPoint.getTarget().getClass().getName();
        String executeKey = execute.value();

        try {
            Object result = joinPoint.proceed();
            HashMap<String, Object> data = new HashMap<String, Object>() {{
                put("data", result);
            }};
            System.out.println(String.format("txId: %s, name: %s, executeKey: %s, data: %s ", TransactionThreadLocal.getTxId(), name, executeKey, data));
            return result;
        } catch (Throwable e) {
            // 某个调用出现异常，设置事务状态为rollback，并立即回滚
            rollback(TransactionThreadLocal.getTxId());
            throw e;
        }
    }

    public void rollback(String txId) throws Throwable {
        System.out.println("开始回滚事务，txId: " + txId);
        List<String> names = Arrays.asList("com.curiophil.javalearn.consistcy.NetworkApi", "com.curiophil.javalearn.consistcy.NetworkApi");
        List<String> executeKeys = Arrays.asList("1", "2");
        List<Map<String, Object>> datas = Arrays.asList(
                new HashMap() {{
                    put("s", "1111111111");
                    put("i", 123);
                    put("l", Arrays.asList(1, 2, 3));
                }},
                new HashMap() {{
                    put("s", "1111111111");
                    put("ll", 123);
                    put("l", Arrays.asList(1, 2, 3));
                }});
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            String executeKey = executeKeys.get(i);
            Map<String, Object> data = datas.get(i);
            Method rollbackMethod = AnnotationFinder.findRollbackMethod(name, executeKey);
            Parameter[] parameters = rollbackMethod.getParameters();
            ArrayList<Object> args = new ArrayList<>();
            if (parameters.length <= 1) {
                if (parameters.length > 0) {
                    args.add(data.get("data"));
                }
            } else {
                Map<String, Object> map = data;
                for (Parameter parameter : parameters) {
                    args.add(map.get(parameter.getName()));
                }
            }
            rollbackMethod.invoke(applicationContext.getBean(Class.forName(name)), args.toArray());
            System.out.println(String.format("txId: %s, name: %s, executeKey: %s, data: %s. rollback succeed. ", TransactionThreadLocal.getTxId(), name, executeKey, data));
        }
        // 回滚结束，更新事务状态为回滚完成
    }
}