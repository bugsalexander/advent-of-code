enum DirKind {
    Left,
    Right,
    Up,
    Down,
}

struct Dir {
    dist: i32,
    kind: DirKind,
}

fn parse(input: &str) -> (impl Iterator<Item = Dir>, impl Iterator<Item = Dir>) {
    let trim = input.trim();
    let parts = input.split("\n");

    let fst = parts.next();
    let snd = parts.next();
    match (fst, snd) {
        (Some(s1), Some(s2)) => (parse_dirs(s1), parse_dirs(s2)),
        _ => panic!("couldn't find first and second parts"),
    }
}

fn parse_dirs(input: &str) -> Box<dyn Iterator<Item = Dir>> {
    let parts = input.split(",");

    fn parse_dir(s: &str) -> Result<Dir, String> {
        return s.parse::<Dir>();
    }
    return Box::new(parts.map(parse_dir as fn(s: &str) -> Result<Dir, String>));
}

use std::str::FromStr;

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
