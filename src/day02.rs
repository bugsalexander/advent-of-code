// day 02 of advent of code 2019

// ––––––––––––––––––––––– notes ––––––––––––––––––––––––
// program is a list of comma-separated integers
// ex 1,2,3,4,5

// index 0 is an opcode
// one of: 1, 2, 99

// 1 - indicates add operation
// read numbers at [1, 2] and add them. leave result at [3]

// 2 - indicates multiply operation (same as add)

// 99 - indicates done

// once done with an opcode, step forward 4 positions

// what value is left at position 0?

// ––––––––––––––––––––– main ––––––––––––––––––––––––
use std::iter::FromIterator;
use std::fs::read_to_string;

#[allow(dead_code)]
fn main() {

    // what pair of inputs produces 19690720?
    let target = 19690720;

    let str = read_to_string("./input/02").unwrap();
    for i1 in 0..100 {
        for i2 in 0..100 {
            let answer = parse_and_compute_zero(&str, i1, i2);
            if answer == target {
                println!("{} {}", i1, i2);
                return;
            }
        }
    }
    // the answer is 60 86.
}


// –––––––––––––––––––––– functions –––––––––––––––––––––––

// the whole shebang
pub fn parse_and_compute_zero(input: &str, inp1: usize, inp2: usize) -> usize {
    let mut vec = parse(input);
    // "1202 program alarm state:"
    // - replace position 1 with value 12
    // - replace position 2 with value 2
    vec[1] = inp1;
    vec[2] = inp2;

    let result = intcompute(&mut vec);

    match result.get(0) {
        Some(result) => *result,
        None => panic!("there was no zero index"),
    }
}

// parse the form 0,1,2,3,3 into a Vec<usize>
pub fn parse(input: &str) -> Vec<usize> {
    let mut i = 0;
    let nums = input.trim().split(",").map(|item| {
        // try and parse each item to usize
        match str::parse::<usize>(item) {
            Ok(n) => {
                i += 1;
                return n;
            }
            Err(e) => panic!("value {} panicked at index {} with error {}", item, i, e),
        }
    });
    Vec::from_iter(nums)
}

// runs an int program
pub fn intcompute(regs: &mut Vec<usize>) -> Vec<usize> {
    let mut index: usize = 0;

    let plus = |n1, n2| n1 + n2;
    let times = |n1, n2| n1 * n2;

    loop {
        match regs.get(index) {
            Some(1) => compute_op(regs, &mut index, plus),
            Some(2) => compute_op(regs, &mut index, times),
            Some(99) => return regs.to_vec(),
            Some(_) => panic!("received unknown opcode"),
            None => panic!("expected instruction, but found none"),
        }
    }
}

// compute a binary operation
pub fn compute_op<F>(vec: &mut Vec<usize>, index: &mut usize, op: F)
where
    F: Fn(usize, usize) -> usize,
{
    match [vec.get(*index + 1), vec.get(*index + 2), vec.get(*index + 3)] {
        [Some(index1), Some(index2), Some(put1)] => {
            let put_index = *put1;
            match [vec.get(*index1), vec.get(*index2)] {
                [Some(num1), Some(num2)] => {
                    vec[put_index] = op(*num1, *num2);
                    // increment index by 4
                    *index += 4;
                },
                _ => panic!("attempted to retrieve an invalid index"),
            }
        }
        _ => panic!("attempted to retrieve an invalid index"),
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_parse() {
        assert_eq!(str::parse::<usize>("0"), Ok(0));
        assert_eq!(parse("1,2,3"), vec![1, 2, 3]);
        assert_eq!(parse("1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,6,1,19,2,19,13,23,1,23,10,27,1,13,27,31,2,31,10,35,1,35,9,39,1,39,13,43,1,13,43,47,1,47,13,51,1,13,51,55,1,5,55,59,2,10,59,63,1,9,63,67,1,6,67,71,2,71,13,75,2,75,13,79,1,79,9,83,2,83,10,87,1,9,87,91,1,6,91,95,1,95,10,99,1,99,13,103,1,13,103,107,2,13,107,111,1,111,9,115,2,115,10,119,1,119,5,123,1,123,2,127,1,127,5,0,99,2,14,0,0"), vec![1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,6,1,19,2,19,13,23,1,23,10,27,1,13,27,31,2,31,10,35,1,35,9,39,1,39,13,43,1,13,43,47,1,47,13,51,1,13,51,55,1,5,55,59,2,10,59,63,1,9,63,67,1,6,67,71,2,71,13,75,2,75,13,79,1,79,9,83,2,83,10,87,1,9,87,91,1,6,91,95,1,95,10,99,1,99,13,103,1,13,103,107,2,13,107,111,1,111,9,115,2,115,10,119,1,119,5,123,1,123,2,127,1,127,5,0,99,2,14,0,0]);
    }

    #[test]
    fn test_compute() {
        assert_eq!(intcompute(&mut vec![1, 0, 0, 0, 99]), vec![2, 0, 0, 0, 99]);
        assert_eq!(intcompute(&mut vec![2, 3, 0, 3, 99]), vec![2, 3, 0, 6, 99]);
        assert_eq!(
            intcompute(&mut vec![2, 4, 4, 5, 99, 0]),
            vec![2, 4, 4, 5, 99, 9801]
        );
        assert_eq!(
            intcompute(&mut vec![1, 1, 1, 4, 99, 5, 6, 0, 99]),
            vec![30, 1, 1, 4, 2, 5, 6, 0, 99]
        );
        assert_eq!(
            intcompute(&mut vec![1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50]),
            vec![3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50]
        );
    }

    #[test]
    fn test_wrapped() {
        assert_eq!(parse_and_compute_zero("1,0,0,0,99,1,1,1,1,1,1,1,0", 12, 2), 2);
        assert_eq!(
            parse_and_compute_zero(
                "
    
    2,12,2,0,99,1,1,1,1,1,1,1,21
    ", 12,2
            ),
            42
        );
    }

    use std::fs::read_to_string;

    #[test]
    fn test_real() {
        let str = read_to_string("./input/02").unwrap();

        let result = parse_and_compute_zero(&str, 12, 2);

        assert_eq!(result, 4330636);
    }
}
