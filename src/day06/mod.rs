mod tests;

use std::collections::{HashMap, HashSet};
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
fn grow_tree(orbits: Orbits) -> () {
    let mut tree = HashMap::new();

    for pair in orbits.0.iter() {
        // if first or second is unknown, then add it.
        insert_if_new(&mut tree, &pair.com, HashSet::new());
        insert_if_new(&mut tree, &pair.sat, HashSet::new());

        // add the second as an orbiter of the first
        tree.get_mut(&pair.com).unwrap().insert(&pair.sat);
    }
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
