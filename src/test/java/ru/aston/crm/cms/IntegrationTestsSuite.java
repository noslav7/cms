package ru.aston.crm.cms;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import ru.aston.crm.cms.repository.ContactInfoRepositoryIntegrationTest;
import ru.aston.crm.cms.repository.CustomerRepositoryIntegrationTest;
import ru.aston.crm.cms.repository.InteractionRepositoryIntegrationTest;
import ru.aston.crm.cms.service.*;

@Suite
@SelectClasses(
        {
                ContactInfoServiceIntegrationTest.class,
                CustomerServiceIntegrationTest.class,
                InteractionServiceIntegrationTest.class,
                ContactInfoRepositoryIntegrationTest.class,
                CustomerRepositoryIntegrationTest.class,
                InteractionRepositoryIntegrationTest.class
        })
public class IntegrationTestsSuite {
}
