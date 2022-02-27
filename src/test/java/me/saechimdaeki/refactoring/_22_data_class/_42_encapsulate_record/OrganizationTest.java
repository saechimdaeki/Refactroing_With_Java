package me.saechimdaeki.refactoring._22_data_class._42_encapsulate_record;

import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationTest {

    @Test
    void name(){
        Organization organization=new Organization("junseong","seoul");
        organization.name();
        organization.country();
    }

}