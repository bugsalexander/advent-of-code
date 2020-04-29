#![allow(dead_code)]

// mod day01;
// mod day02;
// mod day03;
mod day04;

fn main() {
    // what pair of inputs produces 19690720?

    let min = "240298";
    let max = "784956";

    let result = day04::oracle(min, max);

    println!("{:#?}", result);
    // println!("{:#?}", result.total.unwrap());
}
