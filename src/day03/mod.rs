use std::iter::FromIterator;
use std::str::FromStr;

// include the tests module for testing
mod tests;

/// represents a type of direction
#[derive(PartialEq, Debug)]
pub enum DirKind {
    Left,
    Right,
    Up,
    Down,
}

/// represents a direction, with a magnitude
#[derive(PartialEq, Debug)]
pub struct Dir {
    dist: i32,
    kind: DirKind,
}

/// parses the input into two separate lists of directions
pub fn parse(input: &str) -> (Vec<Dir>, Vec<Dir>) {
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
pub fn find_closest(one: Vec<Dir>, two: Vec<Dir>) -> TwoPosns {
    // need to convert the list of deltas to list of positions
    let posns_list = all_posns_fast(one);
    let posns_set = HashSet::<&Posn>::from_iter(posns_list.iter());
    let mut collisions = BinaryHeap::<TwoPosns>::new();

    for posn in all_posns_fast(two) {
        match posns_set.get(&posn) {
            Some(other_posn) => {
                collisions.push(TwoPosns::new(**other_posn, posn));
            }
            _ => {}
        }
    }

    // get rid of 0,0
    collisions.pop();
    return *collisions.peek().unwrap();
}

#[derive(Eq, PartialEq, Hash, Clone, Copy, Debug)]
pub struct TwoPosns {
    pub fst: Posn,
    pub snd: Posn,
    pub total: Option<i32>,
}

impl TwoPosns {
    fn new(fst: Posn, snd: Posn) -> Self {
        TwoPosns {
            fst,
            snd,
            total: match (fst.dist, snd.dist) {
                (Some(d1), Some(d2)) => Some(d1 + d2),
                _ => None,
            },
        }
    }
}

impl PartialOrd for TwoPosns {
    fn partial_cmp(self: &Self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

impl Ord for TwoPosns {
    fn cmp(self: &Self, other: &Self) -> Ordering {
        match (self.total, other.total) {
            (Some(d1), Some(d2)) => d1.cmp(&d2).reverse(),
            _ => panic!("tried to compare two TwoPosns without both having a total dist"),
        }
    }
}

/// represents a cartesian coordinate
#[derive(Eq, Clone, Copy, Debug)]
pub struct Posn {
    pub x: i32,
    pub y: i32,
    pub dist: Option<i32>,
}

/// creates a new posn
impl Posn {
    fn new(x: i32, y: i32) -> Posn {
        Posn::new_dist(x, y, None)
    }

    fn new_dist(x: i32, y: i32, dist: Option<i32>) -> Posn {
        Posn { x, y, dist }
    }

    pub fn manhattan(self: &Self) -> i32 {
        self.x.abs() + self.y.abs()
    }
}

impl PartialEq for Posn {
    fn eq(self: &Self, other: &Self) -> bool {
        self.x == other.x && self.y == other.y
    }
}

use std::hash::{Hash, Hasher};

impl Hash for Posn {
    fn hash<H: Hasher>(self: &Self, state: &mut H) {
        self.x.hash(state);
        self.y.hash(state);
    }
}

use std::cmp::Ord;
use std::cmp::Ordering;

/// compare posns by their manhattan distance
impl Ord for Posn {
    fn cmp(&self, other: &Self) -> Ordering {
        // for comparing with manhattan distance
        // self.manhattan().cmp(&other.manhattan()).reverse()

        // for comparing with path distance
        match (self.dist, other.dist) {
            (Some(d1), Some(d2)) => d1.cmp(&d2),
            _ => panic!("didn't both have a path distance"),
        }
    }
}

impl PartialOrd for Posn {
    fn partial_cmp(&self, other: &Posn) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

// assumes that dirs only have positive distances
fn all_posns_fast(dirs: Vec<Dir>) -> Vec<Posn> {
    let mut next;
    let mut distance = 0;

    let mut current: &Posn = &Posn::new_dist(0, 0, Some(distance));
    distance += 1;
    let mut all: Vec<Posn> = vec![*current];

    for dir in dirs {
        for _ in 0..dir.dist {
            next = match dir.kind {
                DirKind::Left => posn_delta(&current, -1, 0, distance),
                DirKind::Right => posn_delta(&current, 1, 0, distance),
                DirKind::Up => posn_delta(&current, 0, 1, distance),
                DirKind::Down => posn_delta(&current, 0, -1, distance),
            };
            all.push(next);
            current = &next;
            distance += 1;
        }
    }

    all
}

fn posn_delta(p: &Posn, dx: i32, dy: i32, distance: i32) -> Posn {
    Posn::new_dist(p.x + dx, p.y + dy, Some(distance))
}
