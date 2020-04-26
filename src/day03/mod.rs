use std::iter::FromIterator;
use std::str::FromStr;

// include the tests module for testing
mod tests;

/// represents a type of direction
#[derive(PartialEq, Debug)]
enum DirKind {
    Left,
    Right,
    Up,
    Down,
}

/// represents a direction, with a magnitude
#[derive(PartialEq, Debug)]
struct Dir {
    dist: i32,
    kind: DirKind,
}

/// parses the input into two separate lists of directions
fn parse(input: &str) -> (Vec<Dir>, Vec<Dir>) {
    let mut parts = input.trim().split("\n");
    let dirs_1 = parse_dirs(parts.next().unwrap());
    let dirs_2 = parse_dirs(parts.next().unwrap());
    (dirs_1, dirs_2)
}

/// parse the dirs into an Iterator of dirs
fn parse_dirs(input: &str) -> Vec<Dir> {
    // if any of them fail, panic
    Vec::from_iter(input.split(",").map(|s| s.parse::<Dir>().unwrap()))
}

/// parse strings of the form (R|L|U|D)\d+ into Dirs
impl FromStr for Dir {
    type Err = String;
    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match (s.get(0..1), s.get(1..)) {
            (Some(d), Some(len)) => Ok(Dir {
                kind: d.parse::<DirKind>().unwrap(),
                dist: str::parse::<i32>(len).expect("didn't have a length"),
            }),
            _ => Err(String::from(format!("failed to parse into dir: {}", s))),
        }
    }
}

/// parse (R|L|U|D) into a DirKind
impl FromStr for DirKind {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "R" => Ok(DirKind::Right),
            "L" => Ok(DirKind::Left),
            "U" => Ok(DirKind::Up),
            "D" => Ok(DirKind::Down),
            _ => Err(String::from(format!(
                "parse::<DirKind>: expected one of R|L|U|D. got {}",
                s
            ))),
        }
    }
}

// –––––––––– solve the problem

use std::collections::BinaryHeap;
use std::collections::HashSet;

/// take in a string, output the two locations
fn find_closest(input: &str) -> Posn {
    let (one, two) = parse(input);
    // need to convert the list of deltas to list of positions
    let posns_list = all_posns(all_corners(one));
    let posns_set = HashSet::<&Posn>::from_iter(posns_list.iter());
    let mut collisions = BinaryHeap::new();

    for posn in all_posns(all_corners(two)) {
        if posns_set.contains(&posn) {
            collisions.push(posn);
        }
    }

    return collisions.pop().unwrap();
}

/// represents a cartesian coordinate
#[derive(Eq, PartialEq, Hash, PartialOrd, Clone, Copy)]
struct Posn {
    x: i32,
    y: i32,
}

/// creates a new posn
impl Posn {
    fn new(x: i32, y: i32) -> Posn {
        Posn { x, y }
    }
}

use std::cmp::Ord;
use std::cmp::Ordering;

/// compare posns by their manhattan distance
impl Ord for Posn {
    fn cmp(&self, other: &Self) -> Ordering {
        (self.x + self.y).cmp(&(other.x + other.y))
    }
}

/// convert a list of the corners to a list of the posns. assumes the directions start at 0, 0
fn all_posns(corners: Vec<Posn>) -> Vec<Posn> {
    //! this is an innder doc
    let mut posns = Vec::<Posn>::new();
    // okay to unwrap, since should always have 0,0
    let prev = Posn::new(0, 0);

    for corner in corners {
        add_betweens(&mut posns, &prev, &corner);
    }
    return posns;
}

/// finds the corners of all the dirs. does NOT include 0,0
fn all_corners(deltas: Vec<Dir>) -> Vec<Posn> {
    let mut corners: Vec<Posn> = Vec::new();
    let mut prev = Posn::new(0, 0);

    // for each of the directions, produce the next posn
    for dir in deltas {
        let next = match dir.kind {
            DirKind::Left => Posn::new(prev.x - dir.dist, prev.y),
            DirKind::Right => Posn::new(prev.x - dir.dist, prev.y),
            DirKind::Up => Posn::new(prev.x - dir.dist, prev.y),
            DirKind::Down => Posn::new(prev.x - dir.dist, prev.y),
        };
        corners.push(next);
        prev = next;
    }

    return corners;
}

/// adds all the posns between the two to the vector, exclusive of the last. adds them in reverse order
fn add_betweens(place: &mut Vec<Posn>, from: &Posn, to: &Posn) -> () {
    for x in to.x..from.x {
        for y in to.y..from.y {
            place.push(Posn::new(x, y));
        }
    }
}
