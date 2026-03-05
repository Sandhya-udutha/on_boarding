package ug.daes.onboarding.config;

import io.sentry.Sentry;
import io.sentry.SentryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentryConfig {

	@Bean
	public Sentry.OptionsConfiguration<SentryOptions> sentryOptions() {
		return options -> {
			options.setDsn("https://a7939c05d157405f8492ba01fda55c91@monitor.digitaltrusttech.com/18");
			options.setDebug(false);
			options.setTracesSampleRate(1.0);
		};
	}
}
