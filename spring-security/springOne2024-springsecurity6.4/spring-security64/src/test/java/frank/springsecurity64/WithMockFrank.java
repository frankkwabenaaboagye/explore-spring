package frank.springsecurity64;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// we need the annotation to show up at runtime
@Retention(RetentionPolicy.RUNTIME)
// we take off the annotation from the test class itself and bring it here
@WithMockUser("Frank")
public @interface WithMockFrank {
}
