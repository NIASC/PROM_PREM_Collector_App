-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jul 10, 2017 at 10:20 PM
-- Server version: 10.1.22-MariaDB
-- PHP Version: 7.1.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `prom_prem_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `clinics`
--

CREATE TABLE `clinics` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `clinics`
--

INSERT INTO `clinics` (`id`, `name`) VALUES
(1, 'test_clinic'),
(2, 'newclinic'),
(3, 'minklinik'),
(5, 'testclin');

-- --------------------------------------------------------

--
-- Table structure for table `error_messages`
--

CREATE TABLE `error_messages` (
  `id` int(11) NOT NULL,
  `code` int(10) NOT NULL,
  `name` varchar(64) NOT NULL,
  `locale` varchar(8) NOT NULL,
  `message` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `error_messages`
--

INSERT INTO `error_messages` (`id`, `code`, `name`, `locale`, `message`) VALUES
(9, 4, 'NOT_LOGGED_IN', 'en', 'You may not perform this action unless you log in.'),
(2, 2, 'NULL_SELECTED', 'en', 'Nothing selected!'),
(8, 3, 'OPERATION_NOT_PERMITTED', 'en', 'Your account does not have permission to perform this operation.'),
(13, 401, 'QP_INVALID_PID', 'en', 'Valid personal id numbers formats are: yymmddxxxx, yymmdd-xxxx, yyyymmddxxxx, yyyymmdd-xxxx'),
(12, 108, 'REG_REQUEST_FAILED', 'en', 'Could not send request'),
(10, 106, 'UH_ALREADY_ONLINE', 'en', 'That user is already online.'),
(3, 101, 'UH_INVALID_LOGIN', 'en', 'Invalid username or password.'),
(4, 102, 'UH_PR_INVALID_CURRENT', 'en', 'Current password is invalid.'),
(6, 104, 'UH_PR_INVALID_LENGTH', 'en', 'Password must contain between 6 and 32 characters.'),
(5, 103, 'UH_PR_MISMATCH_NEW', 'en', 'New passwords must match.'),
(7, 105, 'UH_PR_PASSWORD_SIMPLE', 'en', 'Password should contain ASCII characters from at least 2 different groups out of: lowercase, uppercase, digits, punctuation (including space).'),
(11, 107, 'UH_SERVER_FULL', 'en', 'The server is full. Try again later.'),
(1, 1, 'UNKNOWN_RESPONSE', 'en', 'Unknown response.');

-- --------------------------------------------------------

--
-- Table structure for table `info_messages`
--

CREATE TABLE `info_messages` (
  `id` int(10) UNSIGNED NOT NULL,
  `code` int(10) NOT NULL,
  `name` varchar(64) CHARACTER SET utf8 NOT NULL,
  `message` varchar(512) CHARACTER SET utf8 NOT NULL,
  `locale` varchar(8) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `info_messages`
--

INSERT INTO `info_messages` (`id`, `code`, `name`, `message`, `locale`) VALUES
(6, 102, 'CURRENT_PASSWORD', 'Enter current password', 'en'),
(4, 2, 'EXIT', 'Exit', 'en'),
(2, 3, 'LOGIN', 'Login', 'en'),
(11, 7, 'LOGOUT', 'Log out', 'en'),
(7, 103, 'NEW_PASSWORD', 'Enter new password', 'en'),
(5, 101, 'NEW_PASS_INFO', 'The new password must be between 6 and 32 characters in length and contain ASCII characters from at least 2 different groups out of: lowercase, uppercase, digits, punctuation (including space).', 'en'),
(3, 4, 'REGISTER', 'Register', 'en'),
(19, 205, 'REG_BODY_DESCRIPTION', 'Registration request from', 'en'),
(15, 201, 'REG_CLINIC_NAME', 'Clinic', 'en'),
(20, 206, 'REG_EMAIL_SIGNATURE', 'This message was sent from the PROM/PREM collector', 'en'),
(18, 204, 'REG_EMAIL_SUBJECT', 'PROM_PREM:Registration request', 'en'),
(22, 207, 'REG_REQUEST_SENT', 'Registration request sent', 'en'),
(17, 203, 'REG_USER_EMAIL', 'E-mail', 'en'),
(16, 202, 'REG_USER_NAME', 'Name', 'en'),
(8, 104, 'RE_NEW_PASSWORD', 'Reenter new password', 'en'),
(1, 1, 'SELECT_OPTION', 'What would you like to do?', 'en'),
(9, 5, 'START_QUESTIONNAIRE', 'Start questionnaire', 'en'),
(13, 106, 'UH_ENTER_PASSWORD', 'Enter password', 'en'),
(12, 105, 'UH_ENTER_USERNAME', 'Enter username', 'en'),
(14, 107, 'UH_UPDATE_PASSWORD', 'Your account have been flagged for password update', 'en'),
(28, 306, 'UI_ENTRY', 'Entry', 'en'),
(29, 307, 'UI_FILLED', 'filled', 'en'),
(27, 305, 'UI_FORM_BACK', 'Back', 'en'),
(24, 302, 'UI_FORM_CONTINUE', 'Continue', 'en'),
(26, 304, 'UI_FORM_NEXT', 'Next', 'en'),
(25, 303, 'UI_FORM_PREVIOUS', 'Previous', 'en'),
(23, 301, 'UI_SELECT_SINGLE', 'Select option', 'en'),
(30, 308, 'UI_UNFILLED', 'unfilled', 'en'),
(10, 6, 'VIEW_STATISTICS', 'View statistics', 'en');

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE `patients` (
  `clinic_id` int(10) NOT NULL,
  `pnr` varchar(20) NOT NULL COMMENT 'Personal number',
  `forename` varchar(256) NOT NULL,
  `lastname` varchar(256) NOT NULL,
  `id` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `patients`
--

INSERT INTO `patients` (`clinic_id`, `pnr`, `forename`, `lastname`, `id`) VALUES
(1, '01230101-1234', 'dsads', 'dsadsa', 13),
(1, '01231010-0000', 'asd', 'dsad', 26),
(1, '09871010-1234', 'asd', 'asd', 11),
(1, '10201010-4315', 'kalle', 'karlsson', 25),
(2, '10510510-7515', 'kalle', 'karlsson', 24),
(1, '12340101-1234', 'asd', 'dsa', 2),
(1, '12340101-5431', 'patienr', 'pasd', 23),
(1, '13450101-1234', 'patiern', 'pasdt2', 10),
(1, '16310512-5011', 'Kalle', 'Karlsson', 17),
(1, '16840910-6431', 'test', 'surname', 22),
(1, '19640819-6321', 'Lars', 'Larsson', 21),
(1, '19780319-6751', 'gunnar', 'andersson', 27),
(1, '19800101-5431', 'Bror', 'Brorsson', 18),
(1, '19820101-4321', 'Råbert', 'Karlsson', 20),
(1, '19841210-0152', 'Anders', 'Andersson', 16),
(1, '19850316-7423', 'Kalle', 'Kula', 19),
(0, '19991012-1234', '', '', 1),
(1, '20000101-0002', 'Anders', 'Andersson', 15),
(1, '20000101-1', 'Sven', 'Svensson', 14),
(1, '20000101-6351', 'Gustav', 'Vasa', 29),
(1, '20051016-5142', 'Johan', 'Johansson', 28),
(1, '43210101-1234', 'pf', 'pl', 9),
(1, '56780101-1234', 'asdas', 'asd', 12);

-- --------------------------------------------------------

--
-- Table structure for table `questionnaire`
--

CREATE TABLE `questionnaire` (
  `id` int(11) UNSIGNED NOT NULL,
  `type` varchar(128) CHARACTER SET utf8 NOT NULL,
  `optional` int(1) UNSIGNED NOT NULL DEFAULT '0',
  `question` varchar(512) CHARACTER SET utf8 NOT NULL,
  `option0` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  `option1` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  `option2` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
  `max_val` int(10) DEFAULT NULL,
  `min_val` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `questionnaire`
--

INSERT INTO `questionnaire` (`id`, `type`, `optional`, `question`, `option0`, `option1`, `option2`, `max_val`, `min_val`) VALUES
(1, 'SingleOption', 0, 'Mobility', 'I have no problems in walking about', 'I have some problems in walking about', 'I am confined to bed', NULL, NULL),
(2, 'SingleOption', 0, 'Self-Care', 'I have no problems with self-care', 'I have some problems with washing or dressing myself', 'I am unable to wash or dress myself', NULL, NULL),
(3, 'SingleOption', 0, 'Usual Activities (e.g. work, study, housework, family or leisure activities)', 'I have no problems with performing my usual activities', 'I have some problems with performing my usual activities', 'I am unable to perform my usual activities', NULL, NULL),
(4, 'SingleOption', 0, 'Pain / Discomfort', 'I have no pain or discomfort', 'I have moderate pain or discomfort', 'I have extreme pain or discomfort', NULL, NULL),
(5, 'SingleOption', 0, 'Anxiety / Depression', 'I am not anxious or depressed', 'I am moderately anxious or depressed', 'I am extremely anxious or depressed', NULL, NULL),
(6, 'Slider', 0, 'Your own health state today.\r\nPlease indicade on this scale how good or bad your own health state is today.\r\nThe best health state you can imagine is marked 10 and the worst health state you can imagine is marked 0.', NULL, NULL, NULL, 10, 0);

-- --------------------------------------------------------

--
-- Table structure for table `questionnaire_answers`
--

CREATE TABLE `questionnaire_answers` (
  `id` int(10) UNSIGNED NOT NULL,
  `date` date NOT NULL,
  `clinic_id` int(11) NOT NULL,
  `question0` varchar(256) CHARACTER SET utf8 NOT NULL,
  `question1` varchar(256) CHARACTER SET utf8 NOT NULL,
  `question2` varchar(256) CHARACTER SET utf8 NOT NULL,
  `question3` varchar(256) CHARACTER SET utf8 NOT NULL,
  `question4` varchar(256) CHARACTER SET utf8 NOT NULL,
  `question5` varchar(128) CHARACTER SET utf8 NOT NULL,
  `patient_pnr` varchar(20) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `questionnaire_answers`
--

INSERT INTO `questionnaire_answers` (`id`, `date`, `clinic_id`, `question0`, `question1`, `question2`, `question3`, `question4`, `question5`, `patient_pnr`) VALUES
(9, '2017-07-04', 1, 'option1', 'option0', 'option2', 'option1', 'option0', '1', '20000101-0001'),
(10, '2017-07-04', 1, 'option1', 'option2', 'option0', 'option2', 'option1', '9', '20000101-0002'),
(11, '2017-07-04', 1, 'option1', 'option2', 'option1', 'option0', 'option2', '3', '19841210-0152'),
(12, '2017-07-04', 1, 'option1', 'option0', 'option2', 'option1', 'option2', '7', '16310512-5011'),
(13, '2017-07-04', 1, 'option1', 'option2', 'option1', 'option2', 'option1', '8', '19800101-5431'),
(14, '2017-07-04', 1, 'option1', 'option2', 'option1', 'option2', 'option2', '4', '19800101-5431'),
(15, '2017-07-04', 1, 'option1', 'option2', 'option1', 'option2', 'option1', '3', '19850316-7423'),
(16, '2017-07-04', 1, 'option1', 'option2', 'option1', 'option0', 'option2', '9', '19820101-4321'),
(17, '2017-07-04', 1, 'option0', 'option2', 'option1', 'option0', 'option2', '10', '19640819-6321'),
(18, '2017-07-04', 1, 'option0', 'option2', 'option1', 'option2', 'option0', '6', '16840910-6431'),
(19, '2017-07-04', 1, 'option0', 'option0', 'option0', 'option0', 'option0', '10', '12340101-5431'),
(20, '2017-07-04', 2, 'option0', 'option2', 'option0', 'option2', 'option0', '0', '10510510-7515'),
(21, '2017-07-04', 1, 'option2', 'option0', 'option2', 'option0', 'option2', '10', '10201010-4315'),
(22, '2017-07-04', 1, 'option2', 'option2', 'option2', 'option2', 'option2', '10', '01231010-0000'),
(23, '2017-07-05', 1, 'option0', 'option0', 'option1', 'option2', 'option1', '8', '19780319-6751'),
(24, '2017-07-10', 1, 'option0', 'option2', 'option1', 'option2', 'option1', '3', '20051016-5142'),
(25, '2017-07-10', 1, 'option2', 'option0', 'option0', 'option2', 'option0', '2', '20000101-6351');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `clinic_id` int(10) NOT NULL,
  `name` varchar(20) NOT NULL,
  `password` varchar(256) NOT NULL,
  `email` varchar(128) NOT NULL,
  `registered` date NOT NULL,
  `salt` varchar(32) NOT NULL,
  `update_password` int(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`clinic_id`, `name`, `password`, `email`, `registered`, `salt`, `update_password`) VALUES
(2, 'asd', 'password', 'asd@localhost', '2017-06-23', '', 1),
(2, 'dsa', 'p4ssw0rd', 'dsa@asd', '2017-06-23', '', 0),
(3, 'mnmamn', 'p4ssw0rd', 'email@emails.com', '2017-07-02', '', 0),
(1, 'test', 'p@ssW0rd', 'test@localhost', '2017-06-16', '', 0),
(1, 'user#0', 'p4ssw0rd', 'user0@localhost', '2017-06-16', '', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `clinics`
--
ALTER TABLE `clinics`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `error_messages`
--
ALTER TABLE `error_messages`
  ADD PRIMARY KEY (`name`),
  ADD KEY `INDEX` (`id`);

--
-- Indexes for table `info_messages`
--
ALTER TABLE `info_messages`
  ADD PRIMARY KEY (`name`),
  ADD KEY `INDEX` (`id`);

--
-- Indexes for table `patients`
--
ALTER TABLE `patients`
  ADD PRIMARY KEY (`pnr`),
  ADD KEY `INDEX` (`id`);

--
-- Indexes for table `questionnaire`
--
ALTER TABLE `questionnaire`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `questionnaire_answers`
--
ALTER TABLE `questionnaire_answers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `INDEX` (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`name`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `clinics`
--
ALTER TABLE `clinics`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `error_messages`
--
ALTER TABLE `error_messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT for table `info_messages`
--
ALTER TABLE `info_messages`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;
--
-- AUTO_INCREMENT for table `patients`
--
ALTER TABLE `patients`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;
--
-- AUTO_INCREMENT for table `questionnaire`
--
ALTER TABLE `questionnaire`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `questionnaire_answers`
--
ALTER TABLE `questionnaire_answers`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
