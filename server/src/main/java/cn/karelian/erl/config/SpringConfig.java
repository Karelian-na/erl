package cn.karelian.erl.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cn.karelian.erl.ErlApplication;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import org.springframework.lang.Nullable;

@Configuration
public class SpringConfig {

	@Bean
	Converter<String, LocalDate> localDateConverter() {
		return new Converter<String, LocalDate>() {
			@Override
			@Nullable
			public LocalDate convert(String str) {
				return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			}
		};
	}

	@Bean
	Converter<String, LocalDateTime> localDateTimeConverter() {
		return new Converter<String, LocalDateTime>() {
			@Override
			@Nullable
			public LocalDateTime convert(String str) {
				return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			}
		};
	}

	@Bean
	public LocalDateTimeSerializer localDateTimeSerializer() {
		return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
	}

	private class MultipartFileSerializer extends JsonSerializer<MultipartFile> {
		@Override
		public void serialize(MultipartFile file, JsonGenerator generator, SerializerProvider provider)
				throws IOException {
			generator.writeString(file.getOriginalFilename() + ":" + file.getSize());
		}
	}

	@Bean
	Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return builder -> builder
				.serializationInclusion(JsonInclude.Include.NON_NULL)
				.serializerByType(MultipartFile.class, new MultipartFileSerializer())
				.serializerByType(LocalDateTime.class, localDateTimeSerializer());
	}

	@Bean
	CorsFilter getCorsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin(ErlApplication.Host);
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.setMaxAge(24L * 60 * 60);
		configuration.setAllowCredentials(true);
		source.registerCorsConfiguration("/**", configuration);
		return new CorsFilter(source);
	}

	@Bean
	WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				WebMvcConfigurer.super.addResourceHandlers(registry);
				registry.addResourceHandler("/files/**")
						.addResourceLocations("file:G:/My Program/electronicresourcelibrary/server/data/files/");
				registry.addResourceHandler("/images/**")
						.addResourceLocations("file:G:/My Program/electronicresourcelibrary/server/data/images/");
			}
		};
	}
}
