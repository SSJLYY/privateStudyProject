package com.dcy.workflow.config;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/10 13:59
 */
@Configuration
public class FlowableConfig {

    @Bean
    public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> globalListenerConfigurer() {
        return engineConfiguration -> {
            //使用mybatis-plus的主键生成器，注意：不会影响act_de开头的表主键生成，因为这是流程设计器的，不是工作流引擎的
            engineConfiguration.setIdGenerator(() -> Convert.toStr(new DefaultIdentifierGenerator().nextId(null)));

        };
    }
}
