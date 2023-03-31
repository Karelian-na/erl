/** @format */

import type { FieldsConfig } from "views/common";

import { userFieldsConfig } from "./UserFieldsConfig";
import { roleFieldsConfig } from "./RoleFieldsConfig";
import { degreeFieldsConfig } from "./DegreeFieldsConfig";
import { courseFieldsConfig } from "./CourseFieldsConfig";
import { databaseFieldsConfig } from "./DatabaseFieldsConfig";
import { permissionFieldsConfig } from "./PermissionFieldsConfig";
import { disciplineFieldsConfig } from "./DisciplineFieldsConfig";
import {
	competitionAwardFieldsConfig,
	conferenceFieldsConfig,
	declarationFieldsConfig,
	paperFieldsConfig,
	patentFieldsConfig,
	projectFieldsConfig,
	teachingAwardFieldsConfig,
} from "./DeclarationFieldsConfig";
import { bookFieldsConfig, bookReserveFieldsConfig } from "./BookFieldsConfig";
import { laboratoryFieldsConfig, laboratoryReserveFieldsConfig } from "./LaboratoryFieldsConfig";
import { bookReservationFieldsConfig, laboratoryReservationFieldsConfig } from "./ReservationFieldsConfig";
import { internshipFieldsConfig, applicationFieldsConfig } from "./InternshipFieldsConfig";
import { majorFieldsConfig } from "./MajorFieldsConfig";
import { teacherFieldsConfig } from "./TeacherFieldsConfig";
import { studentFieldsConfig } from "./StudentFieldsConfig";

const fieldConfigs: Record<string, FieldsConfig> = {
	"/Books/index": bookFieldsConfig,
	"/Books/resindex": bookReserveFieldsConfig,

	"/Laboratories/index": laboratoryFieldsConfig,
	"/Laboratories/resindex": laboratoryReserveFieldsConfig,

	"/Reservations/Book/index": bookReservationFieldsConfig,
	"/Reservations/Book/self/index": bookReservationFieldsConfig,
	"/Reservations/Laboratory/index": laboratoryReservationFieldsConfig,
	"/Reservations/Laboratory/self/index": laboratoryReservationFieldsConfig,

	"/Internships/index": internshipFieldsConfig,
	"/Internships/manage": applicationFieldsConfig,
	"/Internships/appindex": internshipFieldsConfig,

	"/Majors/index": majorFieldsConfig,
	"/Courses/index": courseFieldsConfig,
	"/Courses/Experiment/index": courseFieldsConfig,

	"/Disciplines/index": disciplineFieldsConfig,
	"/Degrees/index": degreeFieldsConfig,

	"/Declarations/index": declarationFieldsConfig,

	"/Users/index": userFieldsConfig,
	"/Teachers/index": teacherFieldsConfig,
	"/Tutors/index": teacherFieldsConfig,
	"/Students/index": studentFieldsConfig,
	"/Posts/index": studentFieldsConfig,
	"/Roles/index": roleFieldsConfig,

	"/Permissions/index": permissionFieldsConfig,

	"/Databases/index": databaseFieldsConfig,

	"/Awards/Teaching/index": teachingAwardFieldsConfig,
	"/Awards/Teacher/index": competitionAwardFieldsConfig,
	"/Awards/Competition/index": competitionAwardFieldsConfig,
	"/Projects/Teaching/index": projectFieldsConfig,
	"/Projects/Research/index": projectFieldsConfig,
	"/Papers/index": paperFieldsConfig,
	"/Papers/Postgraduate/index": paperFieldsConfig,
	"/Patents/index": patentFieldsConfig,
	"/Conferences/index": conferenceFieldsConfig,
};

export default fieldConfigs;
