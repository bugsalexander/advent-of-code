use crate::day02::parse;
mod tests;

// state is composed of
// - instruction pointer: marks the _next_ instruction to execute
// - registers: a bunch of registers, each with a numeric value

// the whole shebang
pub fn parse_and_compute_zero(input: &str) -> usize {
    let mut vec = parse(input);

    let result = intcompute(&mut vec);

    match result.get(0) {
        Some(result) => *result,
        None => panic!("there was no zero index"),
    }
}

use std::io::BufRead;

// runs an int program
pub fn intcompute(regs: &mut Vec<usize>) -> Vec<usize> {
    let mut index: usize = 0;

    let plus = |n1, n2| n1 + n2;
    let times = |n1, n2| n1 * n2;

    // do something with the next line of input.
    let mut current_line: usize = 0;
    let mut do_input = |vec: &mut Vec<usize>, target: usize| {
        // grab the current line, set the dest equal to the result
        // if we don't have a result, then error
        match ith_line(&current_line).and_then(|s| s.parse::<usize>().ok()) {
            Some(number) => {
                vec[target] = number;
                current_line += 1;
                return;
            }
            None => panic!("not enough lines to match input!"),
        }
    };
    // output the thingy at the specified index
    let do_output = |vec: &mut Vec<usize>, target: usize| {
        println!("{}", try_index_once(vec, target));
    };

    loop {
        let opcode_iter = ParamModes::new(&regs.get(index).unwrap().to_string());
        match opcode_iter.opcode {
            Some(1) => compute_binop(regs, &mut index, plus),
            Some(2) => compute_binop(regs, &mut index, times),
            Some(3) => compute_unop(regs, &mut index, &mut do_input),
            Some(4) => compute_unop(regs, &mut index, do_output),
            Some(99) => return regs.to_vec(),
            Some(_) => panic!("received unknown opcode"),
            None => panic!("expected instruction, but found none"),
        }
    }
}

use std::iter::FromIterator;
use std::iter::Iterator;

/// vals contains the parameter modes from right to left
#[derive(Debug, PartialEq, Eq)]
struct ParamModes {
    pub opcode: Option<usize>,
    param_modes: Vec<usize>,
}

use std::convert::TryFrom;

/// creates param modes from a string
impl ParamModes {
    fn new(parammodes_and_opcode: &str) -> ParamModes {
        /// helper function to parse to usize
        fn parse_to_usize(n: char) -> usize {
            usize::try_from(n.to_digit(10).unwrap()).unwrap()
        }

        // the digits, left -> right
        let values = Vec::from_iter(parammodes_and_opcode.chars());
        let length = values.len();

        match values[..] {
            [.., d1, d2] => ParamModes {
                opcode: vec![d1, d2]
                    .into_iter()
                    .collect::<String>()
                    .parse::<usize>()
                    .ok(),
                param_modes: Vec::from_iter(
                    values
                        .into_iter()
                        .take(length - 2)
                        .map(parse_to_usize)
                        .rev(),
                ),
            },
            [d] => ParamModes {
                opcode: d.to_string().parse::<usize>().ok(),
                param_modes: vec![],
            },
            _ => ParamModes {
                opcode: None,
                param_modes: vec![],
            },
        }
    }
}

use std::cmp::Ordering;

// grab the ith line. complexity O(i), excluding overhead of creating iterator over stdin.
fn ith_line(target: &usize) -> Option<String> {
    for (current, line) in std::io::stdin().lock().lines().enumerate() {
        match (current.cmp(target), line) {
            (Ordering::Equal, Ok(input)) => {
                // grab the line, and exit. we are done
                return Some(input);
            }
            (Ordering::Equal, Err(e)) => panic!("error parsing line: {}", e),
            (Ordering::Greater, _) => return None,
            (Ordering::Less, _) => {}
        }
    }

    // if we got this far, then #lines < target.
    None
}

/// compute a binary operation. we assume that all binary operations have a total size of 4.
pub fn compute_binop<F>(vec: &mut Vec<usize>, index: &mut usize, op: F)
where
    F: FnOnce(usize, usize) -> usize,
{
    let num1 = try_index_twice(vec, *index + 1);
    let num2 = try_index_twice(vec, *index + 2);
    let dest = try_index_twice(vec, *index + 3);

    vec[dest] = op(num1, num2);

    // increment by 1 for instruction, 2 for params, 1 for destination = 4
    *index += 4;
}

/// indexes the vec. unwraps once.
fn try_index_once(vec: &[usize], index: usize) -> usize {
    if let Some(result) = vec.get(index) {
        return *result;
    }

    panic!("attempted to retrieve an invalid index");
}

/// indexes the vec, and then indexes again with the result. unwraps twice.
fn try_index_twice(vec: &[usize], index: usize) -> usize {
    return try_index_once(vec, try_index_once(vec, index));
}

// compute a unary operation
pub fn compute_unop<F>(vec: &mut Vec<usize>, index: &mut usize, op: F)
where
    F: FnOnce(&mut Vec<usize>, usize) -> (),
{
    // pass the singular argument to our operation, if we have one.
    let target_index = try_index_once(vec, *index + 1);

    op(vec, target_index);

    // increment by 2 (opcode and single parameter)
    *index += 2;
}
