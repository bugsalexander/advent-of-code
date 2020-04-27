#[cfg(test)]
use super::*;

#[test]
fn test_split() {
    let vec: Vec<&str> = Vec::from_iter("one\ntwo".split("\n"));

    assert_eq!(vec, vec!["one", "two"]);
}

#[test]
fn dirkind_parses() {
    assert_eq!(DirKind::Left, "L".parse::<DirKind>().unwrap());
    assert_eq!(DirKind::Right, "R".parse::<DirKind>().unwrap());
    assert_eq!(DirKind::Up, "U".parse::<DirKind>().unwrap());
    assert_eq!(DirKind::Down, "D".parse::<DirKind>().unwrap());

    assert_eq!(
        Err(String::from(
            "parse::<DirKind>: expected one of R|L|U|D. got x"
        )),
        "x".parse::<DirKind>()
    );
}

#[test]
fn dir_parses() {
    assert_eq!(
        Dir {
            dist: 10,
            kind: DirKind::Left,
        },
        "L10".parse::<Dir>().unwrap()
    );
    assert_eq!(
        Dir {
            dist: 1000,
            kind: DirKind::Up,
        },
        "U001000".parse::<Dir>().unwrap()
    );
}

#[test]
fn dir_list_parses() {
    // R10
    let r10 = Dir {
        dist: 10,
        kind: DirKind::Right,
    };
    // D99
    let d99 = Dir {
        dist: 99,
        kind: DirKind::Down,
    };

    assert_eq!(vec![r10, d99], parse_dirs("R10,D099"));
}

#[test]
fn two_dir_lists_parse() {
    // R10
    let r10 = Dir {
        dist: 10,
        kind: DirKind::Right,
    };
    // D99
    let d99 = Dir {
        dist: 99,
        kind: DirKind::Down,
    };
    assert_eq!((vec![r10], vec![d99]), parse("R10\nD99"));
}

/// experimenting with macros, uwu
#[macro_use]
macro_rules! pos {
    ($x: expr, $y: expr) => {
        Posn::new($x, $y)
    };
}

#[macro_use]
macro_rules! dir {
    ($kind: expr, $dist: expr) => {
        Dir {
            kind: $kind,
            dist: $dist,
        }
    };
}

#[test]
fn test_all_posns() {
    
    // R10
    let right_four = dir!(DirKind::Right, 4);
    let down_three = dir!(DirKind::Down, 3);

    assert_eq!(
        all_posns_fast(vec![right_four, down_three]),
        vec![
            pos![0, 0], 
            pos![1, 0], 
            pos![2, 0], 
            pos![3, 0],
            pos![4, 0],
            pos![4, -1],
            pos![4, -2],
            pos![4, -3],
        ]
    );
}
