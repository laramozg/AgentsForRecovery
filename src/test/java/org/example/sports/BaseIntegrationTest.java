package org.example.sports;

import jakarta.transaction.Transactional;
import org.example.sports.configuration.PostgresAutoConfiguration;
import org.example.sports.repositore.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.example.sports.util.Models.*;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {PostgresAutoConfiguration.class}
)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {

    protected MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private VictimRepository victimRepository;

    @Autowired
    private MutilationRepository mutilationRepository;

    @Autowired
    private ExecutorRepository executorRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FightRepository fightRepository;

    private static boolean dataInitialized = false;

    @BeforeEach
    public void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @BeforeAll
    public void initializeData() {
        if (!dataInitialized) {
            cityRepository.save(CITY());
            victimRepository.save(VICTIM());
            userRepository.save(USER_EXECUTOR());
            executorRepository.save(EXECUTOR());
            userRepository.save(USER_CUSTOMER());
            orderRepository.save(ORDER());
            fightRepository.save(FIGHT());
            mutilationRepository.save(MUTILATION());
            dataInitialized = true;
        }
    }

    public static final String API_PREFIX = "/api/v1/sports/";
}