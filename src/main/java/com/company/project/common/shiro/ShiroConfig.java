package com.company.project.common.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.company.project.common.config.FileUploadProperties;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ShiroConfig
 *
 */
@Configuration
@EnableConfigurationProperties(FileUploadProperties.class)
public class ShiroConfig {

    @Resource
    private FileUploadProperties fileUploadProperties;

    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    @Bean
    public CustomHashedCredentialsMatcher customHashedCredentialsMatcher() {
        return new CustomHashedCredentialsMatcher();
    }

    /**
     * 创建realm
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCredentialsMatcher(customHashedCredentialsMatcher());
        return customRealm;
    }

    @Bean
    public SecurityManager securityManager() {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联realm
        securityManager.setRealm(customRealm());
        return securityManager;
    }


    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        // 整个shiro执行过程： 过滤器、认证、授权
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        LinkedHashMap<String, Filter> filtersMap = new LinkedHashMap<>();
        //用来校验token
        filtersMap.put("token", new CustomAccessControlFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置不会被拦截的链接 顺序判断

        // 放行首页
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/imgs/**", "anon");

        // c端
        filterChainDefinitionMap.put("/user/api/login", "anon");
        filterChainDefinitionMap.put("/user/api/register", "anon");
        filterChainDefinitionMap.put("/sys/dishes/pageList", "anon");
        filterChainDefinitionMap.put("/cook/**", "anon");

        filterChainDefinitionMap.put("/sys/user/login", "anon");
        filterChainDefinitionMap.put("/sys/user/token", "anon");
        filterChainDefinitionMap.put("/sys/getVerify", "anon");
        filterChainDefinitionMap.put("/sys/checkVerify", "anon");
        filterChainDefinitionMap.put("/index/**", "anon");
        filterChainDefinitionMap.put("*.html", "anon");
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/doc.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/v2/api-docs-ext", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/captcha.jpg", "anon");
        filterChainDefinitionMap.put("/csrf", "anon");


        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/layui/**", "anon");
        filterChainDefinitionMap.put("/ui/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/layui-ext/**", "anon");
        filterChainDefinitionMap.put("/app/api/**", "anon");


        filterChainDefinitionMap.put("/**", "token,authc");

        shiroFilterFactoryBean.setLoginUrl("/index/login");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @return org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);


        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }


}

