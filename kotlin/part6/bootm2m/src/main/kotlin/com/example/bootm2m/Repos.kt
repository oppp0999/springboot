package com.example.bootm2m

import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import javax.persistence.*

@Entity
data class Student(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    @ManyToMany
    @JoinTable(
        name = "student_enrolled_in_course",
        joinColumns = arrayOf(JoinColumn(name="student_id")),
        inverseJoinColumns = arrayOf(JoinColumn(name="course_id"))
    )
    val enrolledIn: List<Course> = listOf()
)

interface StudentRepository: CrudRepository<Student, Long>

@Entity
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    @ManyToMany(mappedBy = "enrolledIn")
    val studentsEnrolledIn: List<Student> = listOf()
)

interface CourseRepository: CrudRepository<Course, Long>

data class ViewStudent(
    val id: Long,
    val name: String,
    val coursesEnrolledIn: Iterable<String>
)

fun Student.toView() =
    ViewStudent(id, name, enrolledIn.map{it.name})

data class EnrollInCourse(
    val courseId: Long
)

@RestController
@RequestMapping("students")
class StudentsController(
    val studentRepository: StudentRepository,
    val courseRepository: CourseRepository
) {
    @GetMapping
    fun findAll(): Iterable<ViewStudent> =
        studentRepository.findAll().map { it.toView() }

    @PostMapping("{id}/enroll")
    fun enroll(@PathVariable id : Long, @RequestBody enrollIn: EnrollInCourse): ViewStudent{
        val student = studentRepository.findById(id).orElseThrow {ResponseStatusException(HttpStatus.NOT_FOUND)}
        val course = courseRepository.findById(enrollIn.courseId).orElseThrow {ResponseStatusException(HttpStatus.NOT_FOUND)}

        return studentRepository.save(
                student.copy(
                    enrolledIn = student.enrolledIn.plus(course)
                )
        ).toView()
    }
}

data class ViewCourse(
    val id: Long,
    val name: String,
    val studentsEnrolledIn: Iterable<String>

)

fun Course.toView() =
    ViewCourse(id, name, studentsEnrolledIn.map{it.name})


@RestController
@RequestMapping("courses")
class CourseController(
    val courseRepository: CourseRepository
) {

    @GetMapping
    fun findAll(): Iterable<ViewCourse> =
        courseRepository.findAll().map { it.toView() }
}
