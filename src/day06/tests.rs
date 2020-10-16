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

#[test]
fn count_orbits_example() {
    let input = "\
COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L";
    let count = count_orbits(input.parse::<Orbits>().unwrap());
    assert_eq!(count, 42);
}

#[test]
fn bfs_test() {
    let input = "\
    COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L
K)YOU
I)SAN";

    let orbits: Orbits = input.parse().unwrap();

    assert_eq!(get_com("YOU", &orbits).unwrap(), "K");
    assert_eq!(get_com("SAN", &orbits).unwrap(), "I");

    let count = bfs("YOU", "SAN", &orbits);
    assert_eq!(count, 4);
}
