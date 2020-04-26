#![allow(dead_code)]

use std::fs::read_to_string;

mod day01;
mod day02;
mod day03;

fn main() {

    // what pair of inputs produces 19690720?
    let target = 19690720;

    let str = read_to_string("./input/02").unwrap();
    for i1 in 0..100 {
        for i2 in 0..100 {
            let answer = day02::parse_and_compute_zero(&str, i1, i2);
            if answer == target {
                println!("{} {}", i1, i2);
                return;
            }
        }
    }
}
