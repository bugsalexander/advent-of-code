#[cfg(test)]
use super::*;

#[test]
fn test_parse() {
    use std::fs::read_to_string;
    read_to_string("./input/06")
        .unwrap()
        .parse::<Orbits>()
        .unwrap();
}

#[test]
#[should_panic(expected = "didn't have two strings")]
fn test_parse_fail() {
    "nocloseparenshere".parse::<Orbits>().unwrap();
}
