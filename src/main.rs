#![allow(dead_code)]
#![warn(clippy::all)]
#![allow(clippy::needless_return)]

mod day01;
mod day02;
mod day03;
mod day04;
mod day05;

fn main() {
    let mut input = day02::parse("3,0,4,0,99");
    let result = day05::intcompute(&mut input);
    println!("{:#?}", result);
}
