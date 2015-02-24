<?php



/**
 * Web service test
 * 
 * @param string $string        the test string passed in & to be echoed
 *
 * @return string $string       the test string echoed
 * 
 */
function echo_string($string) {
	return $string;
}
expose_function("test.echo_string", "echo_string", array("string" => array('type' => 'string')), 'A testing method which echos back a string', 'GET', false, false);



/**
 * Web service to get latest blog posts
 * 
 * @param string $limit         number of results to display
 * @param string $offset        offset of list
 *
 * @return string $blog_guid    Blog GUID
 * @return string $title        Title of blog post
 * @return string $name         Name of blog post owner
 * 
 */
function blog_get_latest_posts($limit = 10, $offset = 0) {

	$options = array('type' => 'object', 'subtype' => 'blog', 'limit' => $limit, 'offset' => $offset, );
	$options['metadata_name_value_pairs'] = array( array('name' => 'status', 'value' => 'published'), );

	$posts = elgg_get_entities_from_metadata($options);

	if ($posts) {
		$count = 0;
		foreach ($posts as $single) {
			$blog[$count]['blog_guid'] = $single -> guid;
			$blog[$count]['title'] = htmlspecialchars($single -> title);
			$owner = $single -> getOwnerEntity();
			$blog[$count]['name'] = $owner -> name;
			$blog[$count]['time_created'] = $single -> time_created; 

			$iconPath = $owner -> getIcon('medium');
			if (substr_count($iconPath, "default" . $iconSize . ".gif")) {
				$blog[$count]['avatar'] = null;
			} else {
				$blog[$count]['avatar'] = htmlspecialchars($iconPath);
			}

			$count = $count + 1;
		}

	} else {
		$blog['error']['message'] = elgg_echo("blog:message:noposts");
	}
	return $blog;
}
expose_function('blog.get_latest_posts', "blog_get_latest_posts", array('username' => array('type' => 'string', 'required' => false), 'limit' => array('type' => 'int', 'required' => false), 'offset' => array('type' => 'int', 'required' => false), ), "Web service to get latest blog posts", 'GET', true, true);



/**
 * Web service to get blog posts by tag
 * 
 * @param string $limit         number of results to display
 * @param string $offset        offset of list
 * @param string $tag           category tag
 *
 * @return string $blog_guid    Blog GUID
 * @return string $title        Title of blog post
 * @return string $name         Name of blog post owner
 * 
 */
function blog_get_posts_by_tag($limit = 10, $offset = 0, $tag) {

	$options = array('type' => 'object', 'subtype' => 'blog', 'limit' => $limit, 'offset' => $offset);
	$options['metadata_name_value_pairs'] = array( array('name' => 'status', 'value' => 'published'), );
	$options['query'] = $tag;
	$results = elgg_trigger_plugin_hook('search', 'tags', $options, array());
	$posts['count'] = $results['count'];
	$entries = $results['entities'];

	for ($count = 0; $count < $results['count']; $count++) {

		$posts[$count]['blog_guid'] = $entries[$count] -> guid;
		$posts[$count]['title'] = htmlspecialchars($entries[$count] -> title);
		$owner = $entries[$count] -> getOwnerEntity();
		$posts[$count]['name'] = $owner -> name;
		
	}

	return $posts;
}
expose_function('blog.get_posts_by_tag', "blog_get_posts_by_tag", array('username' => array('type' => 'string', 'required' => false), 'limit' => array('type' => 'int', 'required' => false), 'offset' => array('type' => 'int', 'required' => false), 'tag' => array('type' => 'string', 'required' => yes), ), "Web service to get blog posts by tag", 'GET', true, true);



/**
 * Web service to get blog post using blog GUID
 * 
 * @param string $guid     Blog GUID
 * 
 * @return string $blog_guid         Blog GUID
 * @return string $title             Title of blog post
 * @return string $content           Text of blog post
 * @return string $time_created      Time the blog was created
 * @return string $name              Name of the blog post owner
 * @return string $avatar            Avatar of the blog post owner
 * 
 */
function blog_get_blog($guid) {

	$post = get_entity($guid);

	if ($post) {

		$blog['blog_guid'] = $post -> guid;
		$blog['title'] = htmlspecialchars($post -> title);
		$blog['content'] = $post -> description;
		$blog['time_created'] = $post -> time_created;
		$owner = $post -> getOwnerEntity();
		$blog['name'] = $owner -> name;
		$blog['avatar'] = htmlspecialchars($owner -> getIcon('medium'));

	} else {
		$blog['error']['message'] = elgg_echo("blog:message:noposts");
	}
	return $blog;
}
expose_function('blog.get_blog', "blog_get_blog", array('guid' => array('type' => 'int', 'required' => yes), ), "Web service to get blog post using blog GUID", 'GET', true, true);



/**
 * Web service to get @CMU members by program
 * 
 * @param string $program       Program in 'msit', 'mism', 'msppm', or 'all' members
 * @param string $limit         Currently not used, but can be implemented in the future to pipeline data fetch
 * @param string $offset        Currently not used, but can be implemented in the future to pipeline data fetch
 *
 * @return string $count        A count of the number of entries returned
 * @return string $name         Name of the @CMU member
 * @return string $username     Username of the @CMU member, this is needed for fetching the detailed user profile
 * @return string $avatar       Avatar of the @CMU member
 * 
 */
function get_members_by_program($program = "all", $limit = null, $offset = 0) {

	$iconSize = 'medium';
	$options = array('type' => 'user', 'limit' => $limit, 'offset' => $offset, );

	if ($program == 'msit' || $program == 'mism' || $program == 'msppm') {

		$options['query'] = $program;
		$results = elgg_trigger_plugin_hook('search', 'tags', $options, array());
		$members['count'] = $results['count'];
		$entities = $results['entities'];

		for ($count = 0; $count < $results['count']; $count++) {
			$members[$count]['name'] = $entities[$count] -> name;
			$members[$count]['username'] = $entities[$count] -> username;
			$iconPath = $entities[$count] -> getIcon('small');
			if (substr_count($iconPath, "default" . $iconSize . ".gif")) {
				$members[$count]['avatar'] = null;
			} else {
				$members[$count]['avatar'] = htmlspecialchars($iconPath);
			}
		}

	} else {

		$results = elgg_get_entities($options);
		if ($results) {
			$count = 0;
			foreach ($results as $single) {
				$members[$count]['name'] = $single -> name;
				$members[$count]['username'] = htmlspecialchars($single -> username);
				$iconPath = $single -> getIcon('medium');
				if (substr_count($iconPath, "default" . $iconSize . ".gif")) {
					$members[$count]['avatar'] = null;
				} else {
					$members[$count]['avatar'] = htmlspecialchars($iconPath);
				}
				$members[$count]['email'] = $single -> admin_defined_profile_3;
				$members[$count]['tags'] = $single -> admin_defined_profile_11;
				$count = $count + 1;
			}
			$members['count'] = $count;
		} else {
			$blog['error']['message'] = elgg_echo("blog:message:noposts");
		}
	}
	return $members;
}
expose_function('member.get_members_by_program', "get_members_by_program", array('program' => array('type' => 'string', 'required' => no), ), "Web service to get members by program", 'GET', true, true);



/**
 * Web service to get detailed @CMU member profile
 *
 * @param string $username     Username to get profile information
 *
 * @return string $profile     Array of profile information with labels as the keys
 * 
 */
function member_get_profile($username) {

	$member = get_user_by_username($username);

	if (!$member) {
		throw new InvalidParameterException('registration:usernamenotvalid');
	}

	$profile = elgg_get_config('profile_fields');
	foreach ($profile as $key => $type) {
		$profile[$key] = $member -> $key;
	}

	return $profile;

}
expose_function('member.get_profile', "member_get_profile", array('username' => array('type' => 'string', 'required' => yes), ), "Web service to get detailed member profile", 'GET', true, true);



?>
