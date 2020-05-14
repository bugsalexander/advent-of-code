mod tests;

// state is composed of
// - instruction pointer: marks the _next_ instruction to execute
// - registers: a bunch of registers, each with a numeric value

// the whole shebang
pub fn parse_and_compute(input: &str) {
    intcompute(&mut parse(input));
}

use std::io::BufRead;

// runs an int program
pub fn intcompute(regs: &mut Vec<i32>) -> Vec<i32> {
    let mut index: usize = 0;

    let plus = |n1, n2| n1 + n2;
    let times = |n1, n2| n1 * n2;

    // do something with the next line of input.
    let mut current_line: usize = 0;
    let mut do_input = |vec: &mut Vec<i32>, target: usize| {
        // grab the current line, set the dest equal to the result
        // if we don't have a result, then error
        match ith_line(&current_line).and_then(|s| s.parse::<i32>().ok()) {
            Some(number) => {
                vec[target] = number;
                current_line += 1;
                return;
            }
            None => panic!("not enough lines to match input!"),
        }
    };
    // output the thingy at the specified index
    let do_output = |vec: &mut Vec<i32>, target: usize| {
        println!("{}", try_index_once(vec, target));
    };
    let lt = |n1, n2| if n1 < n2 { 1 } else { 0 };
    let eq = |n1, n2| if n1 == n2 { 1 } else { 0 };

    loop {
        let opcode_iter = ParamModes::new(&regs.get(index).unwrap().to_string());
        match opcode_iter.opcode {
            Some(1) => compute_binop(regs, &mut opcode_iter.param_modes.iter(), &mut index, plus),
            Some(2) => compute_binop(regs, &mut opcode_iter.param_modes.iter(), &mut index, times),
            Some(3) => compute_unop(
                regs,
                &mut opcode_iter.param_modes.iter(),
                &mut index,
                &mut do_input,
            ),
            Some(4) => compute_unop(
                regs,
                &mut opcode_iter.param_modes.iter(),
                &mut index,
                do_output,
            ),
            Some(5) => jump_if_zero(true, regs, &mut opcode_iter.param_modes.iter(), &mut index),
            Some(6) => jump_if_zero(false, regs, &mut opcode_iter.param_modes.iter(), &mut index),
            Some(7) => compute_binop(regs, &mut opcode_iter.param_modes.iter(), &mut index, lt),
            Some(8) => compute_binop(regs, &mut opcode_iter.param_modes.iter(), &mut index, eq),
            Some(99) => return regs.to_vec(),
            Some(op @ _) => panic!("received unknown opcode: {}", op,),
            None => panic!("expected instruction, but found none"),
        }
    }
}

// if the provided is or is not zero (according to yes), then set the instruction pointer.
fn jump_if_zero(
    yes: bool,
    regs: &mut Vec<i32>,
    param_modes: &mut std::slice::Iter<'_, usize>,
    index: &mut usize,
) -> () {
    let cond_value = get_param(regs, param_modes.next(), *index + 1);
    let target = get_param(regs, param_modes.next(), *index + 2);

    let cond = !(regs[cond_value] == 0);
    match [yes, cond] {
        [true, true] | [false, false] => {
            *index = usize::try_from(regs[target]).unwrap();
        }
        _ => {
            // increment by instruction (+1) cond (+1) and target (+1)
            *index += 3;
        }
    }
}

use std::iter::FromIterator;
use std::iter::Iterator;

/// vals contains the parameter modes from right to left
#[derive(Debug, PartialEq, Eq)]
struct ParamModes {
    pub opcode: Option<i32>,
    pub param_modes: Vec<usize>,
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
                    .parse::<i32>()
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
                opcode: d.to_string().parse::<i32>().ok(),
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
pub fn compute_binop<F>(
    vec: &mut Vec<i32>,
    param_modes: &mut std::slice::Iter<'_, usize>,
    index: &mut usize,
    op: F,
) where
    F: FnOnce(i32, i32) -> i32,
{
    let num1 = get_param(vec, param_modes.next(), *index + 1);
    let num2 = get_param(vec, param_modes.next(), *index + 2);
    let dest = get_param(vec, param_modes.next(), *index + 3);

    vec[dest] = op(vec[num1], vec[num2]);

    // increment by 1 for instruction, 2 for params, 1 for destination = 4
    *index += 4;
}

/// produces the index of the corresponding parameter
fn get_param(vec: &[i32], mode: Option<&usize>, index: usize) -> usize {
    // 0 is position mode, 1 is immediate mode
    // if not present, interpreted as 0.
    match mode {
        Some(0) | None => {
            usize::try_from(try_index_once(vec, usize::try_from(index).unwrap())).unwrap()
        }
        Some(1) => index,
        Some(_) => panic!("unknown parameter mode"),
    }
}

/// indexes the vec. unwraps once.
fn try_index_once(vec: &[i32], index: usize) -> i32 {
    if let Some(result) = vec.get(index) {
        return *result;
    }

    panic!("attempted to retrieve an invalid index");
}

// compute a unary operation
pub fn compute_unop<F>(
    vec: &mut Vec<i32>,
    param_modes: &mut std::slice::Iter<'_, usize>,
    index: &mut usize,
    op: F,
) where
    F: FnOnce(&mut Vec<i32>, usize) -> (),
{
    // pass the singular argument to our operation, if we have one.
    let target_index = get_param(vec, param_modes.next(), *index + 1);

    op(vec, target_index);

    // increment by 2 (opcode and single parameter)
    *index += 2;
}

/// parse the form 0,1,2,3,-3 into a Vec<i32>.
/// we have to worry about negatives now.
pub fn parse(input: &str) -> Vec<i32> {
    let mut i = 0;
    let nums = input.trim().split(',').map(|item| {
        // try and parse each item to usize
        match str::parse::<i32>(item) {
            Ok(n) => {
                i += 1;
                return n;
            }
            Err(e) => panic!("value {} panicked at index {} with error {}", item, i, e),
        }
    });
    Vec::from_iter(nums)
}
