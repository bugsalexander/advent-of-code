#![allow(dead_code)]
#![warn(clippy::all)]
#![allow(clippy::needless_return)]

mod day01;
mod day02;
mod day03;
mod day04;
mod day05;
mod day06;

use std::fs::read_to_string;

fn main() {
    let mut input = read_to_string("./input/06").unwrap();
    let result = day06::parse_and_compute(&mut input);
    println!("{}", result);
}
