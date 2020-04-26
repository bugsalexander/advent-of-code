use std::iter::FromIterator;
use std::str::FromStr;

// represents a type of direction
enum DirKind {
    Left,
    Right,
    Up,
    Down,
}

// represents a direction, with a magnitude
struct Dir {
    dist: i32,
    kind: DirKind,
}

// parses the input into two separate lists of directions
fn parse(input: &str) -> (Vec<Dir>, Vec<Dir>) {
    let mut parts = input.trim().split("\n");
    let dirs_1 = parse_dirs(parts.next().unwrap());
    let dirs_2 = parse_dirs(parts.next().unwrap());
    (dirs_1, dirs_2)
}

// parse the dirs into an Iterator of dirs
fn parse_dirs(input: &str) -> Vec<Dir> {
    // if any of them fail, panic
    Vec::from_iter(input.split(",").map(|s| s.parse::<Dir>().unwrap()))
}

// parse strings of the form (R|L|U|D)\d+ into Dirs
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

// parse (R|L|U|D) into a DirKind
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

#[cfg(test)]
mod tests {
    use std::iter::FromIterator;

    #[test]
    fn test_split() {
        let vec: Vec<&str> = Vec::from_iter("one\ntwo".split("\n"));

        assert_eq!(vec, vec!["one", "two"]);
    }
}
