package com.zzq.framework.config;

import com.zzq.common.utils.StringUtils;
import com.zzq.framework.config.properties.SwaggerProperties;
import com.zzq.framework.handler.OpenApiHandler;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.*;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.providers.JavadocProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

/**
 * Swagger 文档配置
 */
@RequiredArgsConstructor
@Configuration
@AutoConfigureBefore(SpringDocConfiguration.class)
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {

    private final SwaggerProperties swaggerProperties;
    private final ServerProperties serverProperties;

    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI openApi() {
        OpenAPI openApi = new OpenAPI();
        // 文档基本信息
        SwaggerProperties.InfoProperties infoProperties = swaggerProperties.getInfo();
        Info info = convertInfo(infoProperties);
        openApi.info(info);
        // 扩展文档信息
        openApi.externalDocs(swaggerProperties.getExternalDocs());
        openApi.tags(swaggerProperties.getTags());
        openApi.paths(swaggerProperties.getPaths());
        openApi.components(swaggerProperties.getComponents());
//        Set<String> keySet = swaggerProperties.getComponents().getSecuritySchemes().keySet();
//        List<SecurityRequirement> list = new ArrayList<>();
//        SecurityRequirement securityRequirement = new SecurityRequirement();
//        keySet.forEach(securityRequirement::addList);
//        list.add(securityRequirement);
//        openApi.security(list);

        return openApi;
    }

    private Info convertInfo(SwaggerProperties.InfoProperties infoProperties) {
        Info info = new Info();
        info.setTitle(infoProperties.getTitle());
        info.setDescription(infoProperties.getDescription());
        info.setContact(infoProperties.getContact());
        info.setLicense(infoProperties.getLicense());
        info.setVersion(infoProperties.getVersion());
        return info;
    }

    /**
     * 自定义 openapi 处理器
     */
    @Bean
    public OpenAPIService openApiBuilder(Optional<OpenAPI> openAPI, SecurityService securityParser, SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils, Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomisers, Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomisers, Optional<JavadocProvider> javadocProvider) {
        return new OpenApiHandler(openAPI, securityParser, springDocConfigProperties, propertyResolverUtils, openApiBuilderCustomisers, serverBaseUrlCustomisers, javadocProvider);
    }

    /**
     * 对已经生成好的 OpenApi 进行自定义操作
     */
    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        String contextPath = serverProperties.getServlet().getContextPath();
        String finalContextPath;
        if (StringUtils.isBlank(contextPath) || "/".equals(contextPath)) {
            finalContextPath = "";
        } else {
            finalContextPath = contextPath;
        }
        // 对所有路径增加前置上下文路径
        return openApi -> {
            Paths oldPaths = openApi.getPaths();
            if (oldPaths instanceof PlusPaths) {
                return;
            }
            PlusPaths newPaths = new PlusPaths();
            oldPaths.forEach((k, v) -> newPaths.addPathItem(finalContextPath + k, v));
            openApi.setPaths(newPaths);
        };
    }

    /**
     * 单独使用一个类便于判断 解决springdoc路径拼接重复问题
     *
     * @author 郅兴开源团队-小黑
     */
    static class PlusPaths extends Paths {

        public PlusPaths() {
            super();
        }
    }

}
