mod tests;

use std::iter::FromIterator;
use std::str::FromStr;

// use the newtype pattern to do parsing
struct Orbits(pub Vec<[String; 2]>);

impl FromStr for Orbits {
    type Err = String;

    // todo : make this Err on failure rather than panic
    // figure out how to map from Vec<Result<T, E>> -> Result<Vec<T>, E>
    fn from_str(input: &str) -> Result<Self, Self::Err> {
        let orbits = Vec::from_iter(input.trim().split('\n').map(|pairstring| {
            match Vec::from_iter(pairstring.split(')')).as_slice() {
                [center, orbiter] => [(*center).to_string(), (*orbiter).to_string()],
                _ => panic!("didn't have two strings: {}", pairstring),
            }
        }));

        Ok(Orbits(orbits))
    }
}
