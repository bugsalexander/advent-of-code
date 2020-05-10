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
fn test_try_index_twice() {
    let vec = vec![1, 2, 3, 2, 1];
    assert_eq!(try_index_twice(&vec, 0), 2);
    assert_eq!(try_index_twice(&vec, 2), 2);
    assert_eq!(try_index_twice(&vec, 3), 3);
    assert_eq!(try_index_twice(&vec, 4), 2);
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
