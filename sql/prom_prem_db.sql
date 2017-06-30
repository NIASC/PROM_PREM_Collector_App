-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 30, 2017 at 07:53 PM
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
(2, 'newclinic');

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
(3, 4, 'REGISTER', 'Request registration', 'en'),
(19, 205, 'REG_BODY_DESCRIPTION', 'Registration request from', 'en'),
(15, 201, 'REG_CLINIC_NAME', 'Clinic', 'en'),
(20, 206, 'REG_EMAIL_SIGNATURE', 'This message was sent from the PROM/PREM collector', 'en'),
(18, 204, 'REG_EMAIL_SUBJECT', 'PROM_PREM:Registration request', 'en'),
(21, 207, 'REG_REQUEST_SENDING', 'Sending registration request', 'en'),
(22, 208, 'REG_REQUEST_SENT', 'Registration request sent', 'en'),
(17, 203, 'REG_USER_EMAIL', 'E-mail', 'en'),
(16, 202, 'REG_USER_NAME', 'Name', 'en'),
(8, 104, 'RE_NEW_PASSWORD', 'Reenter new password', 'en'),
(1, 1, 'SELECT_OPTION', 'What would you like to do?', 'en'),
(9, 5, 'START_QUESTIONNAIRE', 'Start questionnaire', 'en'),
(13, 106, 'UH_ENTER_PASSWORD', 'Enter password', 'en'),
(12, 105, 'UH_ENTER_USERNAME', 'Enter username', 'en'),
(14, 107, 'UH_UPDATE_PASSWORD', 'Your account have been lagged for password update', 'en'),
(28, 306, 'UI_ENTRY', 'Entry', 'en'),
(29, 307, 'UI_FILLED', 'filled', 'en'),
(24, 302, 'UI_FORM_CONTINUE', 'Continue', 'en'),
(27, 305, 'UI_FORM_EXIT', 'Exit', 'en'),
(26, 304, 'UI_FORM_NEXT', 'Next', 'en'),
(25, 303, 'UI_FORM_PREVIOUS', 'Previous', 'en'),
(23, 301, 'UI_SELECT_SINGLE', 'Select option', 'en'),
(30, 308, 'UI_UNFILLED', 'unfilled', 'en'),
(10, 6, 'VIEW_STATISTICS', 'View statistics (for this clinic)', 'en');

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE `patients` (
  `clinic_id` int(10) NOT NULL,
  `pnr` varchar(256) NOT NULL COMMENT 'Personal number',
  `regdate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `error_messages`
--
ALTER TABLE `error_messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `info_messages`
--
ALTER TABLE `info_messages`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
