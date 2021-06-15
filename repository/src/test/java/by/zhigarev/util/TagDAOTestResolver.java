package by.zhigarev.util;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@ComponentScan(basePackages = "by.zhigarev.util")
public class TagDAOTestResolver implements ParameterResolver {


    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if(parameterContext.getParameter().getType() == TagMapper.class){
            return true;
        }
        else {
            return parameterContext.getParameter().getType() == EmbeddedDatabase.class;
        }
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if(parameterContext.getParameter().getType() == TagMapper.class){
            return new TagMapper();
        }
        if(parameterContext.getParameter().getType() == EmbeddedDatabase.class){
            return new EmbeddedDatabaseBuilder()
                    .addDefaultScripts()
                    .setType(EmbeddedDatabaseType.H2)
                    .build();
        }
        return null;
    }


}
