#[cfg(test)]
use super::*;

#[test]
fn test_try_index_once() {
    let vec = vec![0, 1, 2];
    assert_eq!(try_index_once(&vec, 0), 0);
    assert_eq!(try_index_once(&vec, 1), 1);
    assert_eq!(try_index_once(&vec, 2), 2);
}

#[test]
fn test_split_by_empty() {
    let values: Vec<char> = "01".chars().collect();
    assert_eq!(values, vec!['0', '1']);
}

#[test]
fn test_parsing() {
    assert_eq!(
        ParamModes::new("1002"),
        ParamModes {
            opcode: Some(2),
            param_modes: vec![0, 1],
        }
    );
    assert_eq!(
        ParamModes::new("2"),
        ParamModes {
            opcode: Some(2),
            param_modes: vec![]
        }
    );

    assert_eq!(
        ParamModes::new("099"),
        ParamModes {
            opcode: Some(99),
            param_modes: vec![0]
        }
    );
}

#[test]
fn test_integrations() {
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
fn test_param_modes() {
    assert_eq!(
        intcompute(&mut parse("1002,4,3,4,33")),
        vec![1002, 4, 3, 4, 99]
    );
}
