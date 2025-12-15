package com.hospital.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    @Test
    public void testUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setRole(Role.DOCTOR);

        assertEquals(1L, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals(Role.DOCTOR, user.getRole());
    }

    @Test
    public void testAppointment() {
        Appointment appt = new Appointment();
        appt.setStatus(Appointment.AppointmentStatus.BOOKED);
        assertEquals(Appointment.AppointmentStatus.BOOKED, appt.getStatus());
    }

    // Minimal smoke test for other entities to ensure class loading
    @Test
    public void testOtherEntities() {
        assertNotNull(new Prescription());
        assertNotNull(new LabReport());
        assertNotNull(new Vitals());
        assertNotNull(new Availability());
        assertNotNull(new VisitHistory());
    }
}
