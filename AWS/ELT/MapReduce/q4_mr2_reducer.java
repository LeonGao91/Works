import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;
import java.util.TreeSet;

public class q4_mr2_reducer {
	public static void main(String[] args) {
		try {
			System.setIn(new java.io.FileInputStream("q4_sample1_3"));
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String line;

			String curr_location = null;
			TreeSet<Answer> answer_set = null;
			Answer answer = null;

			while ((line = br.readLine()) != null) {
				int limiter_1 = line.indexOf('\t');
				int limiter_2 = line.indexOf(':');

				String location = line.substring(0, limiter_1);
				String hashtag = line.substring(limiter_1 + 1, limiter_2);
				String tweet_ids = line.substring(limiter_2 + 1, line.length());

				int last_comma = -1;
				int next_comma = 0;

				while ((next_comma = tweet_ids.indexOf(',', last_comma + 1)) != -1) {
					String tweet_ids_item = tweet_ids.substring(last_comma + 1, next_comma);
					last_comma = next_comma;
					long tweet_id = Long.parseLong(tweet_ids_item.substring(0, tweet_ids_item.indexOf('|')));
					int hashtag_index = Integer.parseInt(tweet_ids_item.substring(tweet_ids_item.indexOf('|') + 1, tweet_ids_item.length()));

					if (curr_location != null && curr_location.equals(location)) {
						// same location
						if (answer != null && answer.isSameTag(hashtag)) {
							answer.addId(tweet_id, hashtag_index);
						} else {
							if (answer != null) {
								answer_set.add(answer);
							}
							answer = new Answer(hashtag);
							answer.addId(tweet_id, hashtag_index);
						}
					} else {
						if (curr_location != null) {
							if (answer != null) {
								answer_set.add(answer);
							}
							int i = 1;
							for (Answer a : answer_set) {
								StringBuilder sb = new StringBuilder();
								sb.append(curr_location).append('\t');
								sb.append(i).append('\t');
								sb.append(a.toString());
								System.out.println(sb.toString());
								i++;
							}
						}
						curr_location = location;
						answer_set = new TreeSet<Answer>();
						answer = new Answer(hashtag);
						answer.addId(tweet_id, hashtag_index);
					}
				}
			}
			if (curr_location != null) {
				if (answer != null) {
					answer_set.add(answer);
				}
				int i = 1;
				for (Answer a : answer_set) {
					StringBuilder sb = new StringBuilder();
					sb.append(curr_location).append('\t');
					sb.append(i).append('\t');
					sb.append(a.toString());
					System.out.println(sb.toString());
					i++;
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}

class Answer implements Comparable<Answer> {
	String hashtag;
	TreeMap<Long, Integer> id_map;

	@SuppressWarnings("unused")
	private Answer() {
	}

	public Answer(String tag) {
		hashtag = tag;
		id_map = new TreeMap<Long, Integer>();
	}

	public void addId(long tweet_id, int index) {
		if (!id_map.containsKey(tweet_id)) {
			id_map.put(tweet_id, index);
		}
	}

	public boolean isSameTag(String com_tag) {
		return hashtag.equals(com_tag);
	}

	public String getTag() {
		return hashtag;
	}

	public TreeMap<Long, Integer> getTreeMap() {
		return id_map;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(hashtag).append(':');
		for (Long tweet_id : id_map.keySet()) {
			sb.append(tweet_id.toString()).append(',');
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	@Override
	public int compareTo(Answer a) {
		TreeMap<Long, Integer> left_map = this.id_map;
		TreeMap<Long, Integer> right_map = a.id_map;
		if (left_map.size() != right_map.size()) {
			if (left_map.size() < right_map.size()) {
				return 1;
			} else {
				return -1;
			}
		} else {
			long left_long = left_map.firstKey();
			long right_long = right_map.firstKey();
			if (left_long < right_long) {
				return -1;
			} else if (left_long > right_long) {
				return 1;
			} else {
				int left_index = left_map.get(left_long);
				int right_index = right_map.get(right_long);
				if (left_index < right_index) {
					return -1;
				} else if (left_index > right_index) {
					return 1;
				}
			}
		}
		return 0;
	}
}
