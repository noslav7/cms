package ru.aston.crm.cms;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import ru.aston.crm.cms.service.ContactInfoServiceImplTest;
import ru.aston.crm.cms.service.CustomerServiceImplTest;
import ru.aston.crm.cms.service.InteractionServiceImplTest;

@Suite
@SelectClasses(
        {
                ContactInfoServiceImplTest.class,
                CustomerServiceImplTest.class,
                InteractionServiceImplTest.class,
        })
public class UnitTests {
}
