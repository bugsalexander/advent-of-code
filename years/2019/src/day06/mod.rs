mod tests;

use std::collections::{HashMap, HashSet, VecDeque};
use std::iter::FromIterator;
use std::str::FromStr;

/// use the newtype pattern to do parsing
pub struct Orbits(pub Vec<ComSat>);
pub struct ComSat {
    pub com: String,
    pub sat: String,
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
        if let None = tree.get(&*pair.com) {
            tree.insert(&*pair.com, HashSet::new());
        }
        if let None = tree.get(&*pair.sat) {
            tree.insert(&*pair.sat, HashSet::new());
        }

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

pub fn parse_and_compute(input: &str) -> u64 {
    count_orbits(input.parse::<Orbits>().unwrap())
}

pub fn parse_and_compute_day2(input: &str) -> u64 {
    let orbits = input.parse::<Orbits>().unwrap();
    bfs("YOU", "SAN", &orbits)
}

pub fn get_com<'a>(sat: &str, orbits: &'a Orbits) -> Option<&'a str> {
    for comsat in orbits.0.iter() {
        if comsat.sat == sat {
            return Some(&comsat.com);
        }
    }

    None
}

pub(crate) fn bfs(start: &str, end: &str, orbits: &Orbits) -> u64 {
    if *start == *end {
        return 0;
    }

    let rstart = get_com(start, orbits).unwrap();
    let rend = get_com(end, orbits).unwrap();

    let neighbors = build_undirected(&orbits);

    let mut distance = 0;
    let mut seen = HashSet::new();
    let mut queue = HashSet::new();

    // we start at ourselves
    queue.insert(rstart);
    seen.insert(rstart);

    loop {
        let mut next_queue = HashSet::new();

        for current in queue.iter() {
            if **current == *rend {
                return distance;
            } else {
                // insert all the neighbors of current into the queue
                // this is a safe unwrap, as if the node is in the graph, then it exists in the adjacency list
                for neighbor in neighbors.get(current).unwrap().iter() {
                    if !seen.contains(neighbor) {
                        next_queue.insert(*neighbor);
                        seen.insert(*neighbor);
                    }
                }
            }
        }

        distance += 1;
        queue = next_queue;

        if queue.len() == 0 {
            // if we haven't returned, then there is no connection between us and santa
            panic!("there was no connection between us and santa")
        }
    }
}

fn build_undirected<'a>(orbits: &'a Orbits) -> Box<HashMap<&'a str, HashSet<&'a str>>> {
    let mut neighbors: Box<HashMap<&'a str, HashSet<&'a str>>> = Box::new(HashMap::new());

    // build a graph of the neighbors
    for pair in orbits.0.iter() {
        // if first or second is unknown, then add it.
        if let None = neighbors.get(&*pair.com) {
            neighbors.insert(&*pair.com, HashSet::new());
        }
        if let None = neighbors.get(&*pair.sat) {
            neighbors.insert(&*pair.sat, HashSet::new());
        }

        // note the following unwraps are okay, because here ^ we guarantee the values exist

        // add the second as a neighbor of the first
        // need to convert the String (from pair.sat) to an &str, in order to index the hashmap
        // this can be done a few ways, see the following link:
        // https://stackoverflow.com/questions/23975391/how-to-convert-a-string-into-a-static-str
        neighbors.get_mut(&*pair.sat).unwrap().insert(&pair.com);

        // add the first as a neighbor of the second
        neighbors.get_mut(&*pair.com).unwrap().insert(&pair.sat);
    }

    return neighbors;
}
