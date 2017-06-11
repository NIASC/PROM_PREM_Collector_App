# PROM_PREM_Collector
This is an application designed to collect PROM (patient reported outcome measures) and PREM (patient reported experience measures).
It is based on [InformedConsent-app](https://github.com/NIASC/InformedConsent-app).

## Getting Started

### Prerequisits
* [XAMPP](https://www.apachefriends.org/index.html) version 7.1.4 (or similar)
* `git` is recommended

### Downloading the files (XAMPP)
Download the project (as zip or using 'git clone') to you _htdocs_ directory (On a GNU+Linux machine with XAMPP this should be located in _/opt/lampp/_).

### Configuration
yet to be done.

# INSTALL.txt
Below is the information contained in INSTALL.txt. The instructions will be updated later.

### MYSQL
in ./sql/research.sql change localhost:8080 to your site address

Then create database ‘research’ and load from ./sql/research.sql

### Config
in HTML folder is the website
in manage folder is CMS

html/includes/database/mysql/config.php -> Here enter database parameters (password etc)
html/includes/constants.php -> Here web page parameters

#Replace mysite.se with your site address
$config['site_address'] = 'http://mysite.se/research/html/';
#Replace fullpath with full path of directory where research is residing 
$config['base_path'] = '/fullpath/InformedConsent-app/html';

#Also you must chage 
// Consent Data Endpoint
define('CONSENT_URL', 'http://localhost:8080/html/test.php'); - This is the url where PID is sent

// Withdraw Consent Data Endpoint
define('USER_DELETE_URL', 'http://localhost:8080/html/test.php'); - This is the url  where nemid, email is sent if Windraw Consent is pressed

define('SITE_URL', 'http://localhost:8080/InformedConsent-app/html/'); application URL
define('IN_FOLDER', 'research/html/'); here define('IN_FOLDER', '') where applicated is located


### Language
translation of web can be done from here html/includes/language/en.php

### Manage
Parametres for CSM is locotated in this file: manage/includes/config.php
