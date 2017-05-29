-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 29, 2017 at 07:47 PM
-- Server version: 10.1.19-MariaDB
-- PHP Version: 7.0.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `roombooking`
--

-- --------------------------------------------------------

--
-- Table structure for table `modules`
--

CREATE TABLE `modules` (
  `id` int(11) NOT NULL,
  `moduleName` varchar(200) DEFAULT NULL,
  `moduleDesc` varchar(255) DEFAULT NULL,
  `moduleLeader` varchar(200) DEFAULT NULL,
  `users_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `modules`
--

INSERT INTO `modules` (`id`, `moduleName`, `moduleDesc`, `moduleLeader`, `users_id`) VALUES
(1, 'Software Engineering Development', 'Technologies such as java and mysql will be covered', 'Prof. Orhan Gemikonakli', 5),
(2, 'Systems Engineering for Robotics', 'Hardware such as input, output devices will be covered as well as arduino raspberrypi and ros.', 'Dr. Tao Geng', 7),
(3, 'Research Methodology and Professional Project Management', 'Project management skills such as referecing, report writing and planning will be assessed throught this modeul', 'Dr Robert Colson', 10),
(4, 'Web Applications and Databases', 'Web technologies such as php javascript html and css will be used to create websites', 'Dr David Gamez', 9);

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `id` int(11) NOT NULL,
  `RoomName` varchar(200) DEFAULT NULL,
  `type` varchar(200) DEFAULT NULL,
  `size` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`id`, `RoomName`, `type`, `size`) VALUES
(2, 'Room 5', 'Lecture Room', 24),
(3, 'Room 8', 'Networking Room', 30),
(4, 'Room 9', 'Group Room', 12),
(5, 'Room 10', 'Robotics Lab Room', 20),
(6, 'Room 13', 'Lecture Room', 30),
(7, 'Room 6', 'Lecture Room', 24),
(8, 'Room 15', 'Networking Room', 30);

-- --------------------------------------------------------

--
-- Table structure for table `roomsbooked`
--

CREATE TABLE `roomsbooked` (
  `id` int(11) NOT NULL,
  `Room_id` int(11) NOT NULL,
  `Module_Id` int(11) NOT NULL,
  `TimeSlot_id` int(11) NOT NULL,
  `DayBooked` date NOT NULL,
  `User_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `roomsbooked`
--

INSERT INTO `roomsbooked` (`id`, `Room_id`, `Module_Id`, `TimeSlot_id`, `DayBooked`, `User_id`) VALUES
(1, 3, 1, 2, '2017-03-17', 1),
(3, 5, 2, 2, '2017-03-28', 1),
(4, 3, 1, 2, '2017-03-29', 1),
(7, 3, 4, 4, '2017-03-31', 1),
(8, 3, 4, 1, '2017-03-31', 1),
(9, 6, 1, 5, '2017-04-02', 1),
(11, 6, 2, 2, '2017-04-02', 1),
(12, 6, 2, 2, '2017-04-02', 1),
(13, 7, 1, 5, '2017-04-03', 1),
(14, 5, 2, 2, '2017-04-03', 1),
(15, 7, 1, 4, '2017-04-03', 1),
(16, 2, 4, 1, '2017-04-03', 1),
(17, 2, 3, 4, '2017-04-03', 1),
(18, 4, 2, 1, '2017-04-03', 1),
(19, 7, 2, 4, '2017-04-06', 1);

-- --------------------------------------------------------

--
-- Table structure for table `timeslots`
--

CREATE TABLE `timeslots` (
  `id` int(11) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `start_at` time NOT NULL,
  `end_at` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `timeslots`
--

INSERT INTO `timeslots` (`id`, `name`, `start_at`, `end_at`) VALUES
(1, 'Morning Lecture', '09:00:00', '12:00:00'),
(2, 'Noon Lecture', '12:00:00', '15:00:00'),
(3, 'Afternoon Lecture', '15:00:00', '18:00:00'),
(4, 'Evening Lecture', '18:00:00', '21:00:00'),
(5, '10to1', '10:00:00', '13:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `firstname` varchar(50) DEFAULT NULL,
  `surname` varchar(200) DEFAULT NULL,
  `admin` tinyint(1) NOT NULL,
  `Password` varchar(40) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `firstname`, `surname`, `admin`, `Password`, `username`) VALUES
(1, 'Alexander', 'Mifsud', 1, 'test123', 'knave'),
(5, 'Omar', 'Zammit', 0, '13579', 'linuxKing'),
(7, 'Stephen ', 'Hall', 0, 'stephen101', 'roboCop'),
(8, 'James', 'DeCoeur', 1, 'asdfghjkl', 'James123'),
(9, 'Steven', 'Camilieri', 0, '24680', 'webDude'),
(10, 'Mark', 'Borg', 0, 'mark123', 'aiBoss');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `modules`
--
ALTER TABLE `modules`
  ADD PRIMARY KEY (`id`),
  ADD KEY `users_id` (`users_id`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `roomsbooked`
--
ALTER TABLE `roomsbooked`
  ADD PRIMARY KEY (`id`),
  ADD KEY `Room_id` (`Room_id`),
  ADD KEY `Module_Id` (`Module_Id`),
  ADD KEY `TimeSlot_id` (`TimeSlot_id`),
  ADD KEY `User_id` (`User_id`);

--
-- Indexes for table `timeslots`
--
ALTER TABLE `timeslots`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `modules`
--
ALTER TABLE `modules`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `rooms`
--
ALTER TABLE `rooms`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `roomsbooked`
--
ALTER TABLE `roomsbooked`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;
--
-- AUTO_INCREMENT for table `timeslots`
--
ALTER TABLE `timeslots`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `modules`
--
ALTER TABLE `modules`
  ADD CONSTRAINT `modules_ibfk_1` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `roomsbooked`
--
ALTER TABLE `roomsbooked`
  ADD CONSTRAINT `roomsbooked_ibfk_1` FOREIGN KEY (`Room_id`) REFERENCES `rooms` (`id`),
  ADD CONSTRAINT `roomsbooked_ibfk_2` FOREIGN KEY (`Module_Id`) REFERENCES `modules` (`id`),
  ADD CONSTRAINT `roomsbooked_ibfk_3` FOREIGN KEY (`TimeSlot_id`) REFERENCES `timeslots` (`id`),
  ADD CONSTRAINT `roomsbooked_ibfk_4` FOREIGN KEY (`User_id`) REFERENCES `users` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
