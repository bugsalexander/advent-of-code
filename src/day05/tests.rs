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
