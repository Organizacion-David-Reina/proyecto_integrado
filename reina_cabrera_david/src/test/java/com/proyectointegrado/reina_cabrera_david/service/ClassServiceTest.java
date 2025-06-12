package com.proyectointegrado.reina_cabrera_david.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.proyectointegrado.reina_cabrera_david.bean.DanceClass;
import com.proyectointegrado.reina_cabrera_david.bean.ReservationRequest;
import com.proyectointegrado.reina_cabrera_david.bean.Room;
import com.proyectointegrado.reina_cabrera_david.bean.Style;
import com.proyectointegrado.reina_cabrera_david.bean.Teacher;
import com.proyectointegrado.reina_cabrera_david.constants.ErrorConstants;
import com.proyectointegrado.reina_cabrera_david.entity.ClassEntity;
import com.proyectointegrado.reina_cabrera_david.entity.ReservationEntity;
import com.proyectointegrado.reina_cabrera_david.entity.RoomEntity;
import com.proyectointegrado.reina_cabrera_david.entity.StudentEntity;
import com.proyectointegrado.reina_cabrera_david.entity.StyleEntity;
import com.proyectointegrado.reina_cabrera_david.entity.TeacherEntity;
import com.proyectointegrado.reina_cabrera_david.exceptions.InternalServerException;
import com.proyectointegrado.reina_cabrera_david.repository.ClassRepository;
import com.proyectointegrado.reina_cabrera_david.repository.ReservationRepository;
import com.proyectointegrado.reina_cabrera_david.repository.RoomRepository;
import com.proyectointegrado.reina_cabrera_david.repository.StudentsRepository;
import com.proyectointegrado.reina_cabrera_david.repository.StyleRepository;

public class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private StyleRepository styleRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private StudentsRepository studentsRepository;
    
    @Mock
    private EmailService emailService;

    @InjectMocks
    private ClassService classService;

    private DanceClass danceClassRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Style style = Style.builder().id(1).style("Salsa").build();
        Teacher teacher = Teacher.builder().id(1).name("John").lastname("Doe").mail("john@mail.com").nif("1234").build();
        Room room = Room.builder().id(1).roomName("Room A").capacity(10).build();

        danceClassRequest = new DanceClass();
        danceClassRequest.setId(-1);
        danceClassRequest.setStyle(style);
        danceClassRequest.setTeacher(teacher);
        danceClassRequest.setRoom(room);
        danceClassRequest.setLevel("Beginner");
        danceClassRequest.setDay(LocalDate.now());
        danceClassRequest.setStartTime("10:00");
        danceClassRequest.setEndTime("11:00");
    }

    @Test
    public void testSaveClass_Success() {
        when(classRepository.existsByRoomAndDayAndTimeOverlap(anyInt(), any(), any(), any())).thenReturn(false);
        when(classRepository.existsByTeacherAndDayAndTimeOverlap(anyInt(), any(), any(), any())).thenReturn(false);
        when(classRepository.save(any(ClassEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        assertDoesNotThrow(() -> classService.saveClass(danceClassRequest));
        verify(classRepository).save(any(ClassEntity.class));
    }

    @Test
    public void testSaveClass_RoomOccupied_ThrowsException() {
        when(classRepository.existsByRoomAndDayAndTimeOverlap(anyInt(), any(), any(), any())).thenReturn(true);

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> classService.saveClass(danceClassRequest));
        assertEquals(ErrorConstants.CLASS_ALREADY_OCUPPIED, ex.getMessage());
    }

    @Test
    public void testSaveClass_TeacherNotAvailable_ThrowsException() {
        when(classRepository.existsByRoomAndDayAndTimeOverlap(anyInt(), any(), any(), any())).thenReturn(false);
        when(classRepository.existsByTeacherAndDayAndTimeOverlap(anyInt(), any(), any(), any())).thenReturn(true);

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> classService.saveClass(danceClassRequest));
        assertEquals(ErrorConstants.TEACHER_NOT_AVAILABLE, ex.getMessage());
    }

    @Test
    public void testGetClasses_ReturnsMappedClasses() {
        StyleEntity styleEntity = StyleEntity.builder().id(1).style("Salsa").build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).name("John").lastname("Doe").mail("john@mail.com").nif("1234").build();
        RoomEntity roomEntity = RoomEntity.builder().id(1).roomName("Room A").capacity(10).build();

        ClassEntity classEntity = ClassEntity.builder()
                .id(1)
                .style(styleEntity)
                .teacher(teacherEntity)
                .room(roomEntity)
                .level("Beginner")
                .day(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .build();

        when(classRepository.findAllWithRelations()).thenReturn(List.of(classEntity));
        when(reservationRepository.countByClassEntityId(1)).thenReturn(3);

        List<DanceClass> classes = classService.getClasses();

        assertEquals(1, classes.size());
        DanceClass dc = classes.get(0);
        assertEquals(1, dc.getId());
        assertEquals("Salsa", dc.getStyle().getStyle());
        assertEquals("John", dc.getTeacher().getName());
        assertEquals("Room A", dc.getRoom().getRoomName());
        assertEquals(3, dc.getReservations());
        assertEquals("Beginner", dc.getLevel());
        assertEquals(LocalDate.now(), dc.getDay());
        assertEquals("10:00", dc.getStartTime());
        assertEquals("11:00", dc.getEndTime());
    }

    @Test
    public void testGetStyles_ReturnsMappedStyles() {
        StyleEntity styleEntity1 = StyleEntity.builder().id(1).style("Salsa").build();
        StyleEntity styleEntity2 = StyleEntity.builder().id(2).style("Tango").build();

        when(styleRepository.findAll()).thenReturn(Arrays.asList(styleEntity1, styleEntity2));

        List<Style> styles = classService.getStyles();

        assertEquals(2, styles.size());
        assertEquals("Salsa", styles.get(0).getStyle());
        assertEquals("Tango", styles.get(1).getStyle());
    }

    @Test
    public void testGetRooms_ReturnsMappedRooms() {
        RoomEntity roomEntity1 = RoomEntity.builder().id(1).roomName("Room A").capacity(10).build();
        RoomEntity roomEntity2 = RoomEntity.builder().id(2).roomName("Room B").capacity(15).build();

        when(roomRepository.findAll()).thenReturn(Arrays.asList(roomEntity1, roomEntity2));

        List<Room> rooms = classService.getRooms();

        assertEquals(2, rooms.size());
        assertEquals("Room A", rooms.get(0).getRoomName());
        assertEquals(15, rooms.get(1).getCapacity());
    }

    @Test
    public void testUpdateClass_Success() {
        DanceClass updatedRequest = danceClassRequest;
        updatedRequest.setId(1);

        when(classRepository.existsByRoomAndDayAndTimeOverlapExcludingId(anyInt(), any(), any(), any(), anyInt())).thenReturn(false);
        when(classRepository.existsByTeacherAndDayAndTimeOverlapExcludingId(anyInt(), any(), any(), any(), anyInt())).thenReturn(false);

        StyleEntity styleEntity = StyleEntity.builder().id(1).style("Salsa").build();
        TeacherEntity teacherEntity = TeacherEntity.builder().id(1).name("John").lastname("Doe").mail("john@mail.com").nif("1234").build();
        RoomEntity roomEntity = RoomEntity.builder().id(1).roomName("Room A").capacity(10).build();

        ClassEntity existingClass = ClassEntity.builder()
                .id(1)
                .style(styleEntity)
                .teacher(teacherEntity)
                .room(roomEntity)
                .level("Beginner")
                .day(LocalDate.now())
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .build();

        when(classRepository.findById(1)).thenReturn(Optional.of(existingClass));
        when(classRepository.save(existingClass)).thenReturn(existingClass);

        assertDoesNotThrow(() -> classService.updateClass(updatedRequest));
        verify(classRepository).save(existingClass);
    }

    @Test
    public void testUpdateClass_ClassNotFound_ThrowsException() {
        DanceClass updatedRequest = danceClassRequest;
        updatedRequest.setId(1);

        when(classRepository.existsByRoomAndDayAndTimeOverlapExcludingId(anyInt(), any(), any(), any(), anyInt())).thenReturn(false);
        when(classRepository.existsByTeacherAndDayAndTimeOverlapExcludingId(anyInt(), any(), any(), any(), anyInt())).thenReturn(false);
        when(classRepository.findById(1)).thenReturn(Optional.empty());

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> classService.updateClass(updatedRequest));
        assertEquals(ErrorConstants.TEACHER_NOT_AVAILABLE, ex.getMessage());
    }

	@Test
	public void testSaveReservation_Success() {
		ReservationRequest request = new ReservationRequest(1, "1234");

		StudentEntity studentEntity = StudentEntity.builder().id(1).nif("1234").name("Carlos").mail("carlos@mail.com")
				.build();

		StyleEntity styleEntity = StyleEntity.builder().id(1).style("Salsa").build();
		TeacherEntity teacherEntity = TeacherEntity.builder().id(1).name("John").lastname("Doe").build();
		RoomEntity roomEntity = RoomEntity.builder().id(1).roomName("Room A").capacity(2).build();

		ClassEntity classEntity = ClassEntity.builder().id(1).style(styleEntity).teacher(teacherEntity).room(roomEntity)
				.level("Beginner").day(LocalDate.now()).startTime(LocalTime.of(10, 0)).endTime(LocalTime.of(11, 0))
				.build();

		when(studentsRepository.findByNif("1234")).thenReturn(Optional.of(studentEntity));
		when(classRepository.findById(1)).thenReturn(Optional.of(classEntity));
		when(reservationRepository.countByClassEntityId(1)).thenReturn(1);
		when(reservationRepository.findByStudentEntityId(1)).thenReturn(List.of());
		when(reservationRepository.existsByClassEntityIdAndStudentEntityId(1, 1)).thenReturn(false);

		assertDoesNotThrow(() -> classService.saveReservation(request));

		verify(reservationRepository).save(any(ReservationEntity.class));
		verify(emailService).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

    @Test
    public void testSaveReservation_ClassFull_ThrowsException() {
        ReservationRequest request = new ReservationRequest(1, "1234");

        StudentEntity studentEntity = StudentEntity.builder().id(1).nif("1234").build();

        RoomEntity roomEntity = RoomEntity.builder().id(1).roomName("Room A").capacity(1).build();
        ClassEntity classEntity = ClassEntity.builder().id(1).room(roomEntity).day(LocalDate.now()).startTime(LocalTime.of(10, 0)).endTime(LocalTime.of(11, 0)).build();

        when(studentsRepository.findByNif("1234")).thenReturn(Optional.of(studentEntity));
        when(classRepository.findById(1)).thenReturn(Optional.of(classEntity));
        when(reservationRepository.countByClassEntityId(1)).thenReturn(1);

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> classService.saveReservation(request));
        assertEquals(ErrorConstants.CLASS_IS_FULL, ex.getMessage());
    }

    @Test
    public void testSaveReservation_StudentHasConflictingReservation_ThrowsException() {
        ReservationRequest request = new ReservationRequest(1, "1234");

        StudentEntity studentEntity = StudentEntity.builder().id(1).nif("1234").build();

        RoomEntity roomEntity = RoomEntity.builder().id(1).roomName("Room A").capacity(10).build();
        ClassEntity classEntity = ClassEntity.builder().id(1).room(roomEntity).day(LocalDate.now()).startTime(LocalTime.of(10, 0)).endTime(LocalTime.of(11, 0)).build();

        ClassEntity conflictingClass = ClassEntity.builder().id(2).room(roomEntity).day(LocalDate.now()).startTime(LocalTime.of(10, 30)).endTime(LocalTime.of(11, 30)).build();

        ReservationEntity existingReservation = ReservationEntity.builder().id(1).classEntity(conflictingClass).studentEntity(studentEntity).build();

        when(studentsRepository.findByNif("1234")).thenReturn(Optional.of(studentEntity));
        when(classRepository.findById(1)).thenReturn(Optional.of(classEntity));
        when(reservationRepository.countByClassEntityId(1)).thenReturn(0);
        when(reservationRepository.findByStudentEntityId(1)).thenReturn(List.of(existingReservation));

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> classService.saveReservation(request));
        assertEquals(ErrorConstants.STUDENT_ALREADY_HAS_RESERVATION_IN_TIME_SLOT, ex.getMessage());
    }

    @Test
    public void testSaveReservation_StudentAlreadyReserved_ThrowsException() {
        ReservationRequest request = new ReservationRequest(1, "1234");

        StudentEntity studentEntity = StudentEntity.builder().id(1).nif("1234").build();

        RoomEntity roomEntity = RoomEntity.builder().id(1).roomName("Room A").capacity(10).build();
        ClassEntity classEntity = ClassEntity.builder().id(1).room(roomEntity).day(LocalDate.now()).startTime(LocalTime.of(10, 0)).endTime(LocalTime.of(11, 0)).build();

        when(studentsRepository.findByNif("1234")).thenReturn(Optional.of(studentEntity));
        when(classRepository.findById(1)).thenReturn(Optional.of(classEntity));
        when(reservationRepository.countByClassEntityId(1)).thenReturn(0);
        when(reservationRepository.findByStudentEntityId(1)).thenReturn(List.of());
        when(reservationRepository.existsByClassEntityIdAndStudentEntityId(1, 1)).thenReturn(true);

        InternalServerException ex = assertThrows(InternalServerException.class,
                () -> classService.saveReservation(request));
        assertEquals(ErrorConstants.STUDENT_ALREADY_RESERVED, ex.getMessage());
    }

	@Test
	public void testDeleteClass_Success() {
		int classId = 1;
		StyleEntity style = StyleEntity.builder().id(1).style("Salsa").build();
		RoomEntity room = RoomEntity.builder().id(1).roomName("Room A").capacity(10).build();
		TeacherEntity teacher = TeacherEntity.builder().id(1).name("Ana").lastname("López").build();
		ClassEntity classEntity = ClassEntity.builder().id(classId).style(style).room(room).teacher(teacher)
				.day(LocalDate.of(2025, 6, 15)).startTime(LocalTime.of(19, 0)).endTime(LocalTime.of(20, 0)).build();
		StudentEntity student1 = StudentEntity.builder().id(1).name("Carlos").mail("carlos@example.com").build();
		StudentEntity student2 = StudentEntity.builder().id(2).name("Laura").mail("laura@example.com").build();

		when(classRepository.findById(classId)).thenReturn(Optional.of(classEntity));
		when(reservationRepository.findStudentsByClassId(classId)).thenReturn(List.of(student1, student2));

		assertDoesNotThrow(() -> classService.deleteClass(classId));

		verify(emailService).sendEmail(Mockito.eq("carlos@example.com"), Mockito.eq("CANCELACIÓN DE RESERVA"),
				Mockito.contains("Carlos"));
		verify(emailService).sendEmail(Mockito.eq("laura@example.com"), Mockito.eq("CANCELACIÓN DE RESERVA"),
				Mockito.contains("Laura"));

		verify(reservationRepository).deleteByClassEntityId(classId);
		verify(classRepository).delete(classEntity);
	}

	@Test
	void testDeleteReservation_Success() {
		int studentId = 1;
		int classId = 10;
		StudentEntity student = StudentEntity.builder().id(studentId).name("María").mail("maria@example.com").build();
		StyleEntity style = StyleEntity.builder().id(1).style("Bachata").build();
		RoomEntity room = RoomEntity.builder().id(1).roomName("Sala 2").capacity(20).build();
		TeacherEntity teacher = TeacherEntity.builder().id(1).name("Luis").lastname("Pérez").build();
		ClassEntity classEntity = ClassEntity.builder().id(classId).style(style).teacher(teacher).room(room)
				.day(LocalDate.of(2025, 6, 20)).startTime(LocalTime.of(18, 0)).endTime(LocalTime.of(19, 0)).build();

		when(studentsRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(classRepository.findById(classId)).thenReturn(Optional.of(classEntity));

		assertDoesNotThrow(() -> classService.deleteReservation(studentId, classId));

		verify(reservationRepository).deleteByClassEntityIdAndStudentEntityId(classId, studentId);

		verify(emailService).sendEmail(Mockito.eq("maria@example.com"), Mockito.eq("RESERVA DE CLASE"),
				Mockito.contains("María"));
	}
}
