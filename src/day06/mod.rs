mod tests;

use std::collections::{HashMap, HashSet, VecDeque};
use std::iter::FromIterator;
use std::str::FromStr;

/// use the newtype pattern to do parsing
struct Orbits(pub Vec<ComSat>);
struct ComSat {
    com: String,
    sat: String,
}

/// parse a string of orbits of the form xxx)yyy\n... to
/// a list of pairs
impl FromStr for Orbits {
    type Err = String;

    // todo : make this Err on failure rather than panic
    // figure out how to map from Vec<Result<T, E>> -> Result<Vec<T>, E>
    fn from_str(input: &str) -> Result<Self, Self::Err> {
        let orbits = Vec::from_iter(input.trim().split('\n').map(|pairstring| {
            match Vec::from_iter(pairstring.split(')')).as_slice() {
                [center, orbiter] => ComSat {
                    com: (*center).to_string(),
                    sat: (*orbiter).to_string(),
                },
                _ => panic!("didn't have two strings: {}", pairstring),
            }
        }));

        Ok(Orbits(orbits))
    }
}

/// produce a tree structure of the pairs
fn count_orbits(orbits: Orbits) -> u64 {
    let mut tree: HashMap<&str, HashSet<&str>> = HashMap::new();

    for pair in orbits.0.iter() {
        // if first or second is unknown, then add it.
        insert_if_new(&mut tree, &pair.com, HashSet::new());
        insert_if_new(&mut tree, &pair.sat, HashSet::new());

        // add the second as an orbiter of the first
        let com: &str = &pair.com;
        tree.get_mut(com).unwrap().insert(&pair.sat);
    }

    let mut total: u64 = 0;
    let mut cur_distance = 0;
    let mut paths = VecDeque::new();
    paths.push_back("COM");
    // while we have things in the queue, count
    while !paths.is_empty() {
        let mut next_paths = VecDeque::new();
        // for each thing in the queue
        for value in paths.iter() {
            // add neighbors to next, increment total by count
            for neighbor in tree.get(*value).unwrap().iter() {
                next_paths.push_back(*neighbor)
            }
            total += cur_distance;
        }
        cur_distance += 1;
        paths = next_paths;
    }

    total
}

/// inserts a key with specified value into the hashmap, if the key is new.
fn insert_if_new<K, V>(map: &mut HashMap<K, V>, key: K, value: V) -> ()
where
    K: std::cmp::Eq,
    K: std::hash::Hash,
{
    match map.get(&key) {
        Some(_) => {}
        None => {
            map.insert(key, value);
        }
    }
}

pub fn parse_and_compute(input: &str) -> u64 {
    count_orbits(input.parse::<Orbits>().unwrap())
}