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
* In _sql/research.sql_ change _localhost:80_ to your site address.
* Create a new database names _research_ and import _research.sql_ (using XAMPP: In the XAMPP dashboard, go to the _phpMyAdmin_ page and select _Databases_ in the menubar. Then create a new database (the _Collation_ setting does not need to be set)).
* NOTE: The login information is hard-coded to be login:_root_, password:_root_ so you may have to set the password manually to _root_. This maybe break your login to _phpMyAdmin_ so be careful.

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
