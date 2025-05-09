package won.techlog.support

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest

@ExtendWith(DatabaseCleanerExtension::class)
@SpringBootTest
abstract class BaseServiceTest
