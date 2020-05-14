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
    // test that input/output work properly
    day05::parse_and_compute("3,0,4,0,99");

    let mut input = read_to_string("./input/05").unwrap();
    day05::parse_and_compute(&mut input);
}
