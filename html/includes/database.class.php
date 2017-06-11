<?php
/*
--------------------------------------------------------------------------------
 */
class DBM
{

	/**
     * Connect to the mysql database.
     *
     * @param array $parameters The list of parameters used to open the
     *     connection with. It must include the keys 'host', 'username',
     *     'password', 'database'.
     *
     * @return mysqli The instance of the connection.
     */
	public static function open($parameters) {
		
		$database_link = mysqli_connect(
            $parameters['host'], $parameters['username'],
            $parameters['password'], $parameters['database']);
		if (!$database_link)
        {
			return false;
		}
		mysqli_query($database_link, 'SET CHARACTER SET utf8');
		mysqli_query($database_link, "SET NAMES 'utf8'");
		
		return $database_link;
	}
	
	/**
     * Close the connection to the database.
     *
     * @param mysqli The instance of the upen connection.
     *
     * @return bool True if the connection was closed. False on error.
     */
	public static function close($database_link)
    {
		if (!$database_link)
        {
			return false;
		}
		mysqli_close($database_link);
		
		return true;
	}
	
	// Query
	public static function query($query, $database_link = '') {
	
		if ($database_link) {
			return mysqli_query($database_link, $query);
		} else {
			return false;
		}
		
	}
	
	// Num Rows
	public static function numRows($result) {
	
		return mysqli_num_rows($result);
		
	}
	
	// Fetch Object
	public static function fetchObject($result) {
	
		return mysqli_fetch_object($result);
		
	}
	
	// Fetch Array
	public static function fetchArray($result, $intMode = MYSQLI_ASSOC) {
	
		return mysqli_fetch_array($result, $intMode);
		
	}
	
	// Query Data
	public static function queryData($query, $database_link = '') {
	
		if ($database_link) {
			$result = mysqli_query($database_link, $query);
		}
        else
        {
			return false;
		}

        if (is_bool($result))
        {
            /* sometimes mysqli_query returns a boolean which
             * mysqli_num_rows can not handle
             */
            return $result;
        }
		else if (mysqli_num_rows($result))
        {
			return $result;
		}
        else
        {
			return false;
		}
		
	}
	
	// Query Status
	public static function queryStatus($result) {
	
		return mysql_result_status($result);
		
	}
	
	// Insert ID
	public static function insertID($database_link = '') {
		
		if ($database_link) {
			return mysql_insert_id($database_link);
		} else {
			return mysql_insert_id();
		}
		
	}
	
	// Escape
	public static function escape($str) {
	
		return mysql_real_escape_string($str);
		
	}
	
}

?>