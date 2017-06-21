# PROM_PREM_Collector
This is an application designed to collect PROM (patient reported outcome measures) and PREM (patient reported experience measures).
It is based on [InformedConsent-app](https://github.com/NIASC/InformedConsent-app).

## Getting Started

### Prerequisits
* [XAMPP](https://www.apachefriends.org/index.html) version 7.1.4 (or similar). For Starting MySQL.
* [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) (version 5.1.42 included). For MySQL interface to Java
* [GNU JavaMail](https://www.gnu.org/software/classpathx/javamail/javamail.html) (version 1.1.2 included). For email to work.
* [GNU JAF](https://www.gnu.org/software/classpathx/jaf/jaf.html) (version 1.1.1 included). For email to work.
* [GNU inetlib](https://www.gnu.org/software/classpathx/inetlib/inetlib.html) (version 1.1.1 included). For email to work.
* `git` is recommended for downloading the source code.

### Downloading the files (XAMPP)
Download the project (as zip or using `git clone`) to you _htdocs_ directory (On a GNU+Linux machine with XAMPP this should be located in _/opt/lampp/_).

### Configuration [depricated]
* In _sql/research.sql_ change _localhost:80_ to your site address.
* Create a new database names _research_ and import _research.sql_ (using XAMPP: In the XAMPP dashboard, go to the _phpMyAdmin_ page and select _Databases_ in the menubar. Then create a new database (the _Collation_ setting does not need to be set)).
* NOTE: The login information is hard-coded to be login:_root_, password:_root_ so you may have to set the password manually to _root_. This maybe break your login to _phpMyAdmin_ so be careful.
